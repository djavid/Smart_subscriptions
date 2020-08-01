package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.View
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BaseBottomSheetFragment
import com.djavid.smartsubs.utils.KEY_SORT_SCREEN_TYPE
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import org.kodein.di.generic.instance

class SortFragment : BaseBottomSheetFragment(R.layout.fragment_sort) {

    private val presenter: SortContract.Presenter by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kodein = (activity?.application as Application).sortComponent(this)

        getScreenType {
            presenter.init(it)
            dialog?.window?.setWhiteNavigationBar()
        }
    }

    private fun getScreenType(action: (SortContract.ScreenType) -> Unit) {
        requireArguments().getString(KEY_SORT_SCREEN_TYPE)?.let {
            action.invoke(SortContract.ScreenType.valueOf(it))
        }
    }

}