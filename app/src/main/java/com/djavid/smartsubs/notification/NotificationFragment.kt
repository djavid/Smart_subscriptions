package com.djavid.smartsubs.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djavid.smartsubs.Application
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentNotificationBinding
import com.djavid.smartsubs.utils.KEY_NOTIFICATION_ID
import com.djavid.smartsubs.utils.KEY_SUBSCRIPTION_ID
import com.djavid.smartsubs.utils.setWhiteNavigationBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class NotificationFragment : BottomSheetDialogFragment(), KodeinAware {

    private val presenter: NotificationContract.Presenter by instance()

    override lateinit var kodein: Kodein
    private lateinit var binding: FragmentNotificationBinding

    override fun getTheme() = R.style.BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentNotificationBinding.inflate(inflater).apply { binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        kodein = (activity?.application as Application).notificationComponent(this, binding)

        dialog?.window?.setWhiteNavigationBar()
        expandSheet()

        arguments?.let {
            val subId = it.getString(KEY_SUBSCRIPTION_ID)
            val id = it.getLong(KEY_NOTIFICATION_ID, -1)
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