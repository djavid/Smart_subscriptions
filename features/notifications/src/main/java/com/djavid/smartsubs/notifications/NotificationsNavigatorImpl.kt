package com.djavid.smartsubs.notifications

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.common.NotificationsNavigator
import com.djavid.smartsubs.common.utils.Constants

class NotificationsNavigatorImpl(
    private val fm: FragmentManager
) : NotificationsNavigator {

    override fun showNotificationsDialog(subId: String) {
        val bundle = Bundle().apply {
            putString(Constants.KEY_SUBSCRIPTION_ID, subId)
        }

        val dialog = NotificationsFragment().apply {
            arguments = bundle
        }
        dialog.show(fm, dialog.tag)
    }

}