package com.djavid.smartsubs.home

import android.app.Activity
import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.storage.SharedRepository
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class InAppReview(
    private val activity: Activity,
    private val sharedPrefs: SharedRepository,
    private val firebaseLogger: FirebaseLogger
) {

    fun showInAppDialog() {
        val manager = ReviewManagerFactory.create(activity)
        requestReviewFlow(manager) {
            if (it != null) {
                manager.launchReviewFlow(activity, it)
                sharedPrefs.tgInAppReviewTimesShown++
                firebaseLogger.inAppReviewShowTry()
            }
        }
    }

    private fun requestReviewFlow(manager: ReviewManager, onComplete: (ReviewInfo?) -> Unit) {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val reviewInfo = result.result
                onComplete.invoke(reviewInfo)
            } else {
                onComplete.invoke(null)
            }
        }
    }

}