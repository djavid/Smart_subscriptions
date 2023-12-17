package com.djavid.smartsubs.data.interactors

import com.djavid.core.data.BuildConfig

class GetPredefinedSubscriptionRootInteractor {

    fun execute(): String {
        val isDebug = BuildConfig.DEBUG

        return if (isDebug) DB_PREDEFINED_SUBS_TEST_ROOT else DB_PREDEFINED_SUBS_ROOT
    }

    private companion object {
        private const val DB_PREDEFINED_SUBS_ROOT = "predefined_subs"
        private const val DB_PREDEFINED_SUBS_TEST_ROOT = "predefined_subs_test"
    }

}