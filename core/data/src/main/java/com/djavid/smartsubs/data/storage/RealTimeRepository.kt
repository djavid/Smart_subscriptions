package com.djavid.smartsubs.data.storage

import com.djavid.smartsubs.common.coroutines.CancelableCoroutineScope
import com.djavid.smartsubs.data.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.common.models.SubscriptionDao
import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.common.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.data.FirebaseAuthHelper
import com.djavid.smartsubs.data.interactors.GetAuthSubsRootInteractor
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RealTimeRepository(
    private val entityMapper: SubscriptionEntityMapper,
    private val authHelper: FirebaseAuthHelper,
    private val subscriptionEntityMapper: SubscriptionEntityMapper,
    private val getAuthSubsRootInteractor: GetAuthSubsRootInteractor,
) {

    private val subsCache = mutableListOf<SubscriptionDao>()
    private val coroutineScope = CancelableCoroutineScope(Dispatchers.IO)

    init {
        coroutineScope.launch {
            getSubs(allowCache = false) //todo flow
        }
    }

    suspend fun getSubById(id: String, allowCache: Boolean = false): SubscriptionDao? =
        withContext(Dispatchers.IO) {
            if (allowCache) {
                return@withContext subsCache.find { it.id == id }
            }
            val uid = authHelper.getUid() ?: return@withContext null

            return@withContext suspendCoroutine { cont ->
                Firebase.database.reference
                    .child(getAuthSubsRootInteractor.execute())
                    .child(uid)
                    .child(id)
                    .get()
                    .addOnSuccessListener { data ->
                        println(data)
                        val entity = data.getValue(SubscriptionFirebaseEntity::class.java)

                        if (entity != null) {
                            cont.resume(subscriptionEntityMapper.fromFirebaseEntity(entity))
                        } else {
                            cont.resume(null)
                        }
                    }
                    .addOnFailureListener {
                        CrashlyticsLogger.logException(it)
                        cont.resume(null)
                    }
            }
        }

    suspend fun getSubs(allowCache: Boolean = false): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext emptyList()

        if (allowCache && subsCache.isNotEmpty()) return@withContext subsCache

        return@withContext suspendCoroutine { cont ->
            getAuthSubsRoot()
                .child(uid)
                .get()
                .addOnSuccessListener { data ->
                    val subs = data.children
                        .mapNotNull { it.getValue(SubscriptionFirebaseEntity::class.java) }
                        .map { entity -> subscriptionEntityMapper.fromFirebaseEntity(entity) }
                        .map { if (it.hasTrialEnded()) it.copy(trialPaymentDate = null) else it }

                    subsCache.apply { clear(); addAll(subs) }
                    cont.resume(subs)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(emptyList())
                }
        }
    }

    suspend fun editSub(sub: SubscriptionDao): Boolean = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            getAuthSubsRoot()
                .child(uid)
                .child(sub.id)
                .setValue(entityMapper.toFirebaseEntity(sub))
                .addOnSuccessListener {
                    cont.resume(true)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(false)
                }
        }
    }

    suspend fun pushSubs(subs: List<SubscriptionDao>): Boolean = withContext(Dispatchers.IO) {
        var success = true

        subs.forEach {
            success = success && pushSub(it)
        }

        return@withContext success
    }

    suspend fun pushSub(sub: SubscriptionDao): Boolean = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            val ref = getAuthSubsRoot().child(uid).push()
            val key = ref.key

            if (key != null) {
                ref.setValue(entityMapper.toFirebaseEntity(sub.copy(id = key)))
                    .addOnSuccessListener {
                        cont.resume(true)
                    }
                    .addOnFailureListener {
                        CrashlyticsLogger.logException(it)
                        cont.resume(false)
                    }
            } else {
                cont.resume(false)
            }
        }
    }

    suspend fun deleteSubById(id: String): Boolean = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            getAuthSubsRoot()
                .child(uid)
                .child(id)
                .removeValue()
                .addOnSuccessListener {
                    cont.resume(true)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(false)
                }
        }
    }

    private fun getAuthSubsRoot(): DatabaseReference =
        Firebase.database.reference.child(getAuthSubsRootInteractor.execute())
}