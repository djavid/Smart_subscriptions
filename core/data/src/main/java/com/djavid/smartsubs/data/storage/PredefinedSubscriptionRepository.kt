package com.djavid.smartsubs.data.storage

import com.djavid.smartsubs.common.coroutines.CancelableCoroutineScope
import com.djavid.smartsubs.common.entity.PredefinedSubscriptionFirebaseEntity
import com.djavid.smartsubs.common.domain.PredefinedSubscription
import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.data.interactors.GetPredefinedSubscriptionRootInteractor
import com.djavid.smartsubs.data.mappers.PredefinedSubscriptionMapper
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PredefinedSubscriptionRepository(
    private val getPredefinedSubscriptionRootInteractor: GetPredefinedSubscriptionRootInteractor,
    private val mapper: PredefinedSubscriptionMapper
) {

    private val coroutineScope = CancelableCoroutineScope(Dispatchers.Main)
    private val predefinedSubsRoot
        get() = getPredefinedSubscriptionRootInteractor.execute()

    private val predefinedSubEntitiesFlow: Flow<List<PredefinedSubscriptionFirebaseEntity>> = flow {
        emit(fetchPredefinedSubs())
    }.shareIn(coroutineScope, SharingStarted.Eagerly, replay = 1)

    val predefinedSubsWithLogoFlow: Flow<List<PredefinedSubscription>> = predefinedSubEntitiesFlow.map { subs ->
        subs.mapNotNull { sub -> mapper.toModel(sub) }
    }.shareIn(coroutineScope, SharingStarted.Eagerly, replay = 1)

    suspend fun getPredefinedSub(id: String): PredefinedSubscription? {
        return suspendCoroutine { cont ->
            predefinedSubEntitiesFlow.onEach { subs ->
                val sub = subs.find { it.id == id }?.let { entity -> mapper.toModel(entity) }
                cont.resume(sub)
            }.launchIn(coroutineScope)
        }
    }

    private suspend fun fetchPredefinedSubs(): List<PredefinedSubscriptionFirebaseEntity> {
        return suspendCoroutine { cont ->
            Firebase.database.reference.child(predefinedSubsRoot).get()
                .addOnSuccessListener { data ->
                    val subs = data.children.mapNotNull {
                        it.getValue(PredefinedSubscriptionFirebaseEntity::class.java)
                    }
                    cont.resume(subs)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(listOf())
                }
        }
    }
}