package com.djavid.smartsubs.notification

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.navigation.NotificationNavigator
import com.djavid.smartsubs.common.utils.Constants

class NotificationNavigatorImpl(
    private val fm: FragmentManager
) : NotificationNavigator {

    override fun showNotificationDialog(subscriptionId: String, id: Long?) {
        val bundle = Bundle().apply {
            putString(Constants.KEY_SUBSCRIPTION_ID, subscriptionId)
            if (id != null) putLong(Constants.KEY_NOTIFICATION_ID, id)
        }

        val dialog = NotificationFragment().apply {
            arguments = bundle
        }
        dialog.show(fm, dialog.tag)
    }

}