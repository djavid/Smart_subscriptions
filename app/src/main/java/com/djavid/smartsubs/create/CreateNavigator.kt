package com.djavid.smartsubs.create

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID

class CreateNavigator(
    private val fragmentManager: FragmentManager
) : CreateContract.Navigator {

    override fun goToCreateScreen(id: String?) {
        val intent = Bundle().apply {
            if (id != null) {
                putString(KEY_SUBSCRIPTION_ID, id)
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