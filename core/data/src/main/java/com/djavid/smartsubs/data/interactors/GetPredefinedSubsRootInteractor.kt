package com.djavid.smartsubs.data.interactors

import com.djavid.core.data.BuildConfig

class GetPredefinedSubsRootInteractor(
    private val premiumStatusInteractor: GetPremiumSubscriptionStatusInteractor
) {

    fun execute(): String {
        val isPremium = premiumStatusInteractor.execute()
        val isDebug = BuildConfig.DEBUG
        val root = if (isPremium) DB_PREDEFINED_SUBS_PREMIUM_ROOT else DB_PREDEFINED_SUBS_ROOT
        val testRoot = root + DB_PREDEFINED_SUBS_TEST_SUFFIX

        return if (isDebug) testRoot else root
    }

    private companion object {
        private const val DB_PREDEFINED_SUBS_ROOT = "predefined_subs"
        private const val DB_PREDEFINED_SUBS_PREMIUM_ROOT = "predefined_subs_premium"
        private const val DB_PREDEFINED_SUBS_TEST_SUFFIX = "_test"
    }

}