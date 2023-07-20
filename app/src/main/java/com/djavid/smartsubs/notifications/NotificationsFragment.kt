package com.djavid.smartsubs.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentNotificationsBinding
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class NotificationsFragment : BottomSheetDialogFragment(), DIAware {

    private val presenter: NotificationsContract.Presenter by instance()
    private lateinit var binding: FragmentNotificationsBinding

    override lateinit var di: DI

    override fun getTheme() = R.style.BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentNotificationsBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).notificationsComponent(this@NotificationsFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setWhiteNavigationBar()
        expandSheet()

        arguments?.let {
            val subId = it.getString(KEY_SUBSCRIPTION_ID)
            presenter.init(subId)
        }
    }

    private fun expandSheet() {
        view?.post {
            val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

}