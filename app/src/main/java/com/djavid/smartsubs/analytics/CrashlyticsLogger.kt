package com.djavid.smartsubs.analytics

import com.djavid.smartsubs.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLogger {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    fun logError(message: String, tag: String, error: Throwable) {
        if (!BuildConfig.DEBUG) {
            crashlytics.log("W/$tag: $message")
            crashlytics.recordException(error)
        }
    }

    fun log(message: String) {
        if (!BuildConfig.DEBUG) {
            crashlytics.log(message)
        }
    }

    fun logException(e: Throwable?) {
        if (e != null && !BuildConfig.DEBUG) {
            crashlytics.recordException(e)
        }
    }
}