package com.djavid.smartsubs.storage

import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.models.PredefinedSubFirebaseEntity
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.root.FirebaseAuthHelper
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RealTimeRepository(
    private val entityMapper: SubscriptionEntityMapper,
    private val authHelper: FirebaseAuthHelper,
    private val subscriptionEntityMapper: SubscriptionEntityMapper
) {

    private val predefinedSubsCache = mutableListOf<PredefinedSubFirebaseEntity>()

    suspend fun getSubById(id: String): SubscriptionDao? = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext null

        return@withContext suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
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

    suspend fun getAllPredefinedSubs(allowCache: Boolean = false): List<PredefinedSubFirebaseEntity> =
        withContext(Dispatchers.IO) {
            if (predefinedSubsCache.isNotEmpty() && allowCache) {
                return@withContext predefinedSubsCache
            }

            return@withContext suspendCoroutine { cont ->
                Firebase.database.reference
                    .child(DB_PREDEFINED_SUBS_ROOT)
                    .get()
                    .addOnSuccessListener { data ->
                        val subs = data.children
                            .mapNotNull { it.getValue(PredefinedSubFirebaseEntity::class.java) }

                        predefinedSubsCache.clear()
                        predefinedSubsCache.addAll(subs)
                        cont.resume(subs)
                    }
                    .addOnFailureListener {
                        CrashlyticsLogger.logException(it)
                        cont.resume(emptyList())
                    }
            }
        }

    suspend fun getPredefinedSub(id: String): PredefinedSubFirebaseEntity? =
        withContext(Dispatchers.IO) {
            return@withContext suspendCoroutine { cont ->
                Firebase.database.reference
                    .child(DB_PREDEFINED_SUBS_ROOT)
                    .orderByChild("id")
                    .equalTo(id)
                    .get()
                    .addOnSuccessListener { data ->
                        val subs = data.children.mapNotNull {
                            it.getValue(PredefinedSubFirebaseEntity::class.java)
                        }

                        cont.resume(subs.firstOrNull())
                    }
                    .addOnFailureListener {
                        CrashlyticsLogger.logException(it)
                        cont.resume(null)
                    }
            }
        }

    suspend fun getSubs(): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext emptyList()

        return@withContext suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .get()
                .addOnSuccessListener { data ->
                    val subs = data.children
                        .mapNotNull { it.getValue(SubscriptionFirebaseEntity::class.java) }
                        .map { entity -> subscriptionEntityMapper.fromFirebaseEntity(entity) }

                    cont.resume(subs)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(emptyList())
                }
        }
    }

    suspend fun saveSubs(subs: List<SubscriptionDao>): Boolean = withContext(Dispatchers.IO) {
        var success = true

        subs.forEach {
            success = success && pushSub(it)
        }

        return@withContext success
    }

    suspend fun editSub(sub: SubscriptionDao): Boolean = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
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

    suspend fun pushSub(sub: SubscriptionDao): Boolean = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            val ref = Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .push()
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
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
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

    suspend fun updateTrialSubs(): Unit = withContext(Dispatchers.IO) {
        getSubs().forEach {
            if (it.trialPaymentDate != null && LocalDate.now().isAfter(it.trialPaymentDate)) {
                val sub = it.copy(paymentDate = it.trialPaymentDate, trialPaymentDate = null)
                editSub(sub)
            }
        }
    }

    suspend fun isEmpty(): Boolean? = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext false

        return@withContext suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .get()
                .addOnSuccessListener { data ->
                    cont.resume(!data.hasChildren())
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(null)
                }
        }
    }

//    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) { //todo use this to show suggestions
//        queries.getCategories().executeAsList().mapNotNull { it.category }
//    }

    companion object {
        const val DB_SUBS_AUTH_ROOT = "subs_auth"
        const val DB_PREDEFINED_SUBS_ROOT = "predefined_subs"
    }

}