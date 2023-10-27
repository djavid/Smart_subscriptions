package com.djavid.smartsubs.data.storage

import com.djavid.smartsubs.data.models.PredefinedSubFirebaseEntity
import com.djavid.smartsubs.data.models.SubscriptionDao
import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.coroutines.CancelableCoroutineScope
import com.djavid.smartsubs.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.models.PredefinedSuggestionItem
import com.djavid.smartsubs.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.root.FirebaseAuthHelper
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RealTimeRepository(
    private val entityMapper: SubscriptionEntityMapper,
    private val authHelper: FirebaseAuthHelper,
    private val subscriptionEntityMapper: SubscriptionEntityMapper,
    private val storageRepository: CloudStorageRepository
) {

    init {
        CancelableCoroutineScope(Dispatchers.IO).launch {
            getAllPredefinedSubsWithLogo()
            getSubs(allowCache = false)
        }
    }

    private val predefinedSubsCache = mutableListOf<PredefinedSubFirebaseEntity>()
    private val subsCache = mutableListOf<SubscriptionDao>()

    suspend fun getSubById(id: String, allowCache: Boolean = false): SubscriptionDao? =
        withContext(Dispatchers.IO) {
            if (allowCache) {
                return@withContext subsCache.find { it.id == id }
            }
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

    suspend fun getAllPredefinedSubsWithLogo(allowCache: Boolean = false): List<PredefinedSuggestionItem> =
        withContext(Dispatchers.IO) {
            getAllPredefinedSubs(allowCache).mapNotNull {
                val bytes = storageRepository.getSubLogoBytes(it.logoUrl) ?: return@mapNotNull null
                PredefinedSuggestionItem(it.id, it.title, bytes, it.abbreviations)
            }
        }

    private suspend fun getAllPredefinedSubs(allowCache: Boolean = false): List<PredefinedSubFirebaseEntity> =
        withContext(Dispatchers.IO) {
            if (allowCache && predefinedSubsCache.isNotEmpty()) {
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
                        cont.resume(subs.toMutableList())
                    }
                    .addOnFailureListener {
                        CrashlyticsLogger.logException(it)
                        cont.resume(mutableListOf())
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

    suspend fun getSubs(allowCache: Boolean = false): List<SubscriptionDao> = withContext(Dispatchers.IO) {
        val uid = authHelper.getUid() ?: return@withContext emptyList()

        if (allowCache && subsCache.isNotEmpty()) return@withContext subsCache

        return@withContext suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .get()
                .addOnSuccessListener { data ->
                    val subs = data.children
                        .mapNotNull { it.getValue(SubscriptionFirebaseEntity::class.java) }
                        .map { entity -> subscriptionEntityMapper.fromFirebaseEntity(entity) }

                    subsCache.clear()
                    subsCache.addAll(subs)
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

    /**
     * Save trial payment date as a payment date if trial ended
     */
    suspend fun updateTrialSubs(): Unit = withContext(Dispatchers.IO) {
        getSubs().forEach {
            val isTrialEnded = it.trialPaymentDate != null && LocalDate.now().isAfter(it.trialPaymentDate)

            if (isTrialEnded) {
                editSub(it.copy(paymentDate = it.trialPaymentDate, trialPaymentDate = null))
            }
        }
    }

//    suspend fun isEmpty(): Boolean? = withContext(Dispatchers.IO) {
//        val uid = authHelper.getUid() ?: return@withContext false
//
//        return@withContext suspendCoroutine { cont ->
//            Firebase.database.reference
//                .child(DB_SUBS_AUTH_ROOT)
//                .child(uid)
//                .get()
//                .addOnSuccessListener { data ->
//                    cont.resume(!data.hasChildren())
//                }
//                .addOnFailureListener {
//                    CrashlyticsLogger.logException(it)
//                    cont.resume(null)
//                }
//        }
//    }

//    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) { //todo use this to show suggestions
//        queries.getCategories().executeAsList().mapNotNull { it.category }
//    }

    companion object {
        const val DB_SUBS_AUTH_ROOT = "subs_auth"
        const val DB_PREDEFINED_SUBS_ROOT = "predefined_subs"
    }

}