package com.djavid.smartsubs.create

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.core.ui.R
import com.djavid.smartsubs.common.CreateNavigator
import com.djavid.smartsubs.utils.Constants

class CreateNavigator(
    private val fragmentManager: FragmentManager
) : CreateNavigator {

    override fun goToCreateScreen(id: String?) {
        val intent = Bundle().apply {
            if (id != null) {
                putString(Constants.KEY_SUBSCRIPTION_ID, id)
            }
        }

        fragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                CreateFragment().apply { arguments = intent },
                CreateFragment::class.java.simpleName
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

}