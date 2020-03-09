package com.djavid.smartsubs.create

import android.transition.AutoTransition
import android.view.View
import androidx.fragment.app.FragmentManager
import com.djavid.smartsubs.R

class CreateNavigator(
    private val fragmentManager: FragmentManager
) : CreateContract.Navigator {

    override fun goToCreateScreen(addBtn: View) {
        fragmentManager
            .beginTransaction()
            .addSharedElement(addBtn, "add_transition")
            .replace(R.id.fragmentContainer, CreateFragment().apply {
                sharedElementEnterTransition = AutoTransition()
                sharedElementReturnTransition = AutoTransition()
            })
            .addToBackStack(null)
            .commit()
    }

}