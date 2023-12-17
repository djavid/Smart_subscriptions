package com.djavid.smartsubs.data.interactors

import com.djavid.core.data.BuildConfig

class GetAuthSubsRootInteractor {
    fun execute(): String {
        val isDebug = BuildConfig.DEBUG

        return if (isDebug) DB_SUBS_AUTH_TEST_ROOT else DB_SUBS_AUTH_ROOT
    }

    private companion object {
        const val DB_SUBS_AUTH_ROOT = "subs_auth"
        const val DB_SUBS_AUTH_TEST_ROOT = "subs_auth_test"
    }
}