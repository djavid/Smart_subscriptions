package com.djavid.smartsubs.create

import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class CreateNavigator(
    private val fragmentManager: FragmentManager
) : CreateContract.Navigator {

    override fun goToCreateScreen() {
        fragmentManager
            .beginTransaction()
            .add(
                R.id.fragmentContainer,
                CreateFragment(),
                CreateFragment::class.java.simpleName
            )
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

}