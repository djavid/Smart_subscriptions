package com.djavid.smartsubs.common.navigation

interface HomeNavigator {
    fun goToHome()
}

interface CreateNavigator {
    fun goToCreateScreen(id: String? = null)
}

interface NotificationsNavigator {
    fun showNotificationsDialog(subId: String)
}

interface SortNavigator {
    fun openSortScreen()
    fun openSortByScreen()
    fun goBack()
}

interface SubscriptionNavigator {
    fun goToSubscription(id: String, isRoot: Boolean = false) //isRoot means that homeFragment doesn't exist in backstack
}

interface SubListNavigator {
    fun goToSubListScreen()
}

interface CurrencyListNavigator {
    fun goToCurrencyListScreen()
}

interface NotificationNavigator {
    fun showNotificationDialog(subscriptionId: String, id: Long? = null)
}