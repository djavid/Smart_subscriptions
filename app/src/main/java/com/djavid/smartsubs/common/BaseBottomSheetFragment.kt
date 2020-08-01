package com.djavid.smartsubs.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class BaseBottomSheetFragment(
    private val layoutRes: Int
) : BottomSheetDialogFragment(), KodeinAware {

    override lateinit var kodein: Kodein

    override fun getTheme() = R.style.BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

}