package com.djavid.smartsubs.notification

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.utils.KEY_NOTIFICATION_ID
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID

class NotificationNavigator(
    private val fm: FragmentManager
) : NotificationContract.Navigator {

    override fun showNotificationDialog(subscriptionId: Long, id: Long?) {
        val bundle = Bundle().apply {
            putLong(KEY_SUBSCRIPTION_ID, subscriptionId)
            if (id != null) putLong(KEY_NOTIFICATION_ID, id)
        }

        val dialog = NotificationFragment().apply {
            arguments = bundle
        }
        dialog.show(fm, dialog.tag)
    }

}