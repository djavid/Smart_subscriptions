package com.djavid.smartsubs.common

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware

abstract class BaseBottomSheetFragment: BottomSheetDialogFragment(), DIAware {

    override lateinit var di: DI

}