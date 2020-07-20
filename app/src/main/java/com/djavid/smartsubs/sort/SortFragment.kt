package com.djavid.smartsubs.sort

import android.os.Bundle
import android.view.View
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.common.BaseBottomSheetFragment
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.kodein.di.generic.instance

class SortFragment : BaseBottomSheetFragment(R.layout.fragment_sort) {

    private val presenter: SortContract.Presenter by instance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kodein = (activity?.application as Application).sortComponent(this)
        presenter.init()

        dialog?.window?.setWhiteNavigationBar()
        expandSheet()
    }

    private fun expandSheet() { //todo
        view?.post {
            dialog?.findViewById<View>(R.id.design_bottom_sheet)?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(it)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

}