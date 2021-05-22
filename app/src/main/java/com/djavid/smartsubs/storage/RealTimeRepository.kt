package com.djavid.smartsubs.storage

import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.models.SubscriptionFirebaseEntity
import com.djavid.smartsubs.root.FirebaseAuthHelper
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RealTimeRepository(
    private val entityMapper: SubscriptionEntityMapper,
    private val authHelper: FirebaseAuthHelper,
    private val subscriptionEntityMapper: SubscriptionEntityMapper
) {

    suspend fun getSubs(): List<SubscriptionDao> {
        val uid = authHelper.getUid() ?: return emptyList()

        return suspendCoroutine { cont ->
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

    suspend fun saveSub(sub: SubscriptionDao): Boolean {
        val uid = authHelper.getUid() ?: return false

        return suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .child(sub.id.toString())
                .setValue(entityMapper.toEntity(sub))
                .addOnSuccessListener {
                    cont.resume(true)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(false)
                }
        }
    }

    suspend fun hasLoadedUserSubs(): Boolean {
        val uid = authHelper.getUid() ?: return false

        return suspendCoroutine { cont ->
            Firebase.database.reference
                .child(DB_SUBS_AUTH_ROOT)
                .child(uid)
                .get()
                .addOnSuccessListener { data ->
                    cont.resume(data.children.count() > 0)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(false)
                }
        }
    }

    companion object {
        const val DB_SUBS_AUTH_ROOT = "subs_auth"
    }

}