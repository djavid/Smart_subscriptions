package com.djavid.smartsubs.analytics

import com.djavid.core.analytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLogger {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun logException(e: Throwable?) {
        if (e != null && !BuildConfig.DEBUG) {
            crashlytics.recordException(e)
        } else {
            e?.printStackTrace()
        }
    }
}