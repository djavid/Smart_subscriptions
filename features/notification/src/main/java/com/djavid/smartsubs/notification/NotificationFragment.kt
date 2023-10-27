package com.djavid.smartsubs.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.databinding.FragmentNotificationBinding
import com.djavid.common.KEY_NOTIFICATION_ID
import com.djavid.common.KEY_SUBSCRIPTION_ID
import com.djavid.common.setWhiteNavigationBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class NotificationFragment : BottomSheetDialogFragment(), DIAware {

    private val presenter: NotificationContract.Presenter by instance()

    private lateinit var binding: FragmentNotificationBinding
    override lateinit var di: DI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentNotificationBinding.inflate(inflater).apply {
            binding = this
            di = (requireActivity().application as Application).notificationComponent(this@NotificationFragment, binding)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.setWhiteNavigationBar()
        expandSheet()

        arguments?.let {
            val subId = it.getString(com.djavid.common.KEY_SUBSCRIPTION_ID)
            val id = it.getLong(com.djavid.common.KEY_NOTIFICATION_ID, -1)
            presenter.init(subId, if (id == -1L) null else id)
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