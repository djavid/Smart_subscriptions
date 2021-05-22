package com.djavid.smartsubs.notifications

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID

class NotificationsNavigator(
    private val fm: FragmentManager
) : NotificationsContract.Navigator {

    override fun showNotificationsDialog(subId: String) {
        val bundle = Bundle().apply {
            putString(KEY_SUBSCRIPTION_ID, subId)
        }

        val dialog = NotificationsFragment().apply {
            arguments = bundle
        }
        dialog.show(fm, dialog.tag)
    }

}