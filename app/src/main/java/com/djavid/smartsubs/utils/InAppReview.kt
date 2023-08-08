package com.djavid.smartsubs.utils

import android.app.Activity
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.storage.SharedRepository
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InAppReview(
    private val activity: Activity,
    private val sharedPrefs: SharedRepository,
    private val firebaseLogger: FirebaseLogger
) {

    suspend fun showInAppDialog() = withContext(Dispatchers.Main) {
        val manager = ReviewManagerFactory.create(activity)
        val reviewInfo = requestReviewFlow(manager)

        if (reviewInfo != null) {
            manager.launchReviewFlow(activity, reviewInfo)
            sharedPrefs.inAppReviewTimesShown++
            firebaseLogger.inAppReviewShowTry()
        }
    }

    private suspend fun requestReviewFlow(manager: ReviewManager) = suspendCoroutine {
        manager.requestReviewFlow().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                it.resume(result.result)
            } else {
                it.resume(null)
            }
        }
    }

}