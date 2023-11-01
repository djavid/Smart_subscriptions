package com.djavid.smartsubs.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.features.notifications.databinding.FragmentNotificationsBinding
import com.djavid.smartsubs.common.SmartSubsApplication
import com.djavid.smartsubs.common.utils.Constants
import com.djavid.smartsubs.common.utils.setWhiteNavigationBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class NotificationsFragment : BottomSheetDialogFragment(), DIAware {

    private val presenter: NotificationsContract.Presenter by instance()
    private lateinit var binding: FragmentNotificationsBinding

    override lateinit var di: DI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentNotificationsBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as SmartSubsApplication).notificationsComponent(this@NotificationsFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setWhiteNavigationBar()
        expandSheet()

        arguments?.let {
            val subId = it.getString(Constants.KEY_SUBSCRIPTION_ID)
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