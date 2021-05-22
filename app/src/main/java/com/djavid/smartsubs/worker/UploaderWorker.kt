package com.djavid.smartsubs.worker

import android.content.Context
import androidx.work.*
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.models.SubscriptionDao
import com.djavid.smartsubs.storage.RealTimeRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class UploaderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KodeinAware {

    private val subsRepository by instance<SubscriptionsRepository>()
    private val realTimeRepository by instance<RealTimeRepository>()

    override var kodein: Kodein = (appContext as Application).uploaderComponent()

    override suspend fun doWork(): Result {
        val subs = subsRepository.getSubs()

        if (!realTimeRepository.hasLoadedUserSubs()) {
            saveSubsToRealTimeDb(subs)
        }

        return if (saveSubsToRealTimeDb(subs.filter { !it.isLoaded })) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    private suspend fun saveSubsToRealTimeDb(subs: List<SubscriptionDao>): Boolean {
        var failedCount = 0

        subs.forEach { sub ->
            val uploaded = realTimeRepository.saveSub(sub)

            if (uploaded) {
                subsRepository.editSub(sub.copy(isLoaded = true))
            } else {
                failedCount++
            }
        }

        return failedCount == 0
    }

    companion object {
        fun enqueueWork(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<UploaderWorker>()
                .setConstraints(constraints)
                .addTag(this::class.java.name)
                .build()

            WorkManager.getInstance(context).cancelAllWorkByTag(this::class.java.name)
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}