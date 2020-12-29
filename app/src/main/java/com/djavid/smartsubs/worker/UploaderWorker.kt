package com.djavid.smartsubs.worker

import android.content.Context
import androidx.work.*
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionEntityMapper
import com.djavid.smartsubs.models.SubscriptionDao
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UploaderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KodeinAware {

    override var kodein: Kodein = (appContext as Application).uploaderComponent()

    private val subsRepository by instance<SubscriptionsRepository>()
    private val entityMapper by instance<SubscriptionEntityMapper>()
    private val coroutineScope: CoroutineScope by instance()

    override suspend fun doWork(): Result {
        val subs = subsRepository.getSubs().filter { !it.isLoaded }
        val id = inputData.getString(INSTALLATION_ID)

        return if (id != null) {
            var failedCount = 0

            subs.forEach {
                val result = saveSubToRealtimeDb(it, id)
                if (!result) failedCount++
            }

            if (failedCount == 0)
                Result.success()
            else
                Result.failure()
        } else {
            Result.failure()
        }
    }

    private suspend fun saveSubToRealtimeDb(sub: SubscriptionDao, firebaseId: String) =
        suspendCoroutine<Boolean> { cont ->
            Firebase.database.reference
                .child("subs")
                .child(firebaseId)
                .child(sub.id.toString())
                .setValue(entityMapper.toEntity(sub))
                .addOnSuccessListener { _ ->
                    coroutineScope.launch {
                        subsRepository.editSub(sub.copy(isLoaded = true))
                        cont.resume(true)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    cont.resume(false)
                }
        }

    companion object {

        private const val TAG = "UploadWorker"
        private const val INSTALLATION_ID = "installation_id"

        fun enqueueWork(context: Context, installationId: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = Data.Builder()
                .putString(INSTALLATION_ID, installationId)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<UploaderWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .addTag(TAG)
                .build()

            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}