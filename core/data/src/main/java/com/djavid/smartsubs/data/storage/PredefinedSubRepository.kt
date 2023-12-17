package com.djavid.smartsubs.data.storage

import com.djavid.smartsubs.common.coroutines.CancelableCoroutineScope
import com.djavid.smartsubs.common.models.PredefinedSubFirebaseEntity
import com.djavid.smartsubs.common.models.PredefinedSuggestionItem
import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.data.interactors.GetPredefinedSubsRootInteractor
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

class PredefinedSubRepository(
    private val storageRepository: CloudStorageRepository,
    private val getPredefinedSubsRootInteractor: GetPredefinedSubsRootInteractor,
) {

    private val coroutineScope = CancelableCoroutineScope(Dispatchers.IO)

    private val predefinedSubsRoot
        get() = getPredefinedSubsRootInteractor.execute()

    private val predefinedSubEntitiesFlow: Flow<List<PredefinedSubFirebaseEntity>> =
        flow { emit(fetchPredefinedSubs()) }.shareIn(coroutineScope, SharingStarted.Eagerly, replay = 1)

    val predefinedSubsWithLogoFlow: Flow<List<PredefinedSuggestionItem>> = predefinedSubEntitiesFlow.map { subs ->
        subs.mapNotNull { sub ->
            val url = storageRepository.getSubLogoUrl(sub.logoUrl) ?: return@mapNotNull null

            PredefinedSuggestionItem(
                subId = sub.id,
                title = sub.title,
                logoUrl = url,
                abbreviations = sub.abbreviations
            )
        }
    }.shareIn(coroutineScope, SharingStarted.Eagerly, replay = 1)

    suspend fun getPredefinedSub(id: String): PredefinedSubFirebaseEntity? = suspendCoroutine { cont ->
        predefinedSubEntitiesFlow.onEach { subs ->
            cont.resume(subs.find { it.id == id })
        }.launchIn(coroutineScope)
    }

    private suspend fun fetchPredefinedSubs(): List<PredefinedSubFirebaseEntity> {
        return suspendCoroutine { cont ->
            Firebase.database.reference.child(predefinedSubsRoot)
                .get()
                .addOnSuccessListener { data ->
                    val subs = data.children.mapNotNull { it.getValue(PredefinedSubFirebaseEntity::class.java) }
                    cont.resume(subs)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(listOf())
                }
        }
    }
}