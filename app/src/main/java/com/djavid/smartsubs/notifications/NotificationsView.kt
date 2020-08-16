package com.djavid.smartsubs.notifications

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.subscription.NotificationsAdapter
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class NotificationsView(
    private val viewRoot: View
) : NotificationsContract.View {

    private lateinit var presenter: NotificationsContract.Presenter
    private lateinit var adapter: NotificationsAdapter

    init {
        viewRoot.notifs_addNotifBtn.setOnClickListener { presenter.onAddNotification() }
    }

    override fun init(presenter: NotificationsContract.Presenter) {
        this.presenter = presenter

        setupNotificationsAdapter()
    }

    private fun setupNotificationsAdapter() {
        adapter = NotificationsAdapter(viewRoot.context, presenter::onEditNotification, presenter::onNotifCheckChanged)
        viewRoot.notifs_recycler.adapter = adapter
        viewRoot.notifs_recycler.layoutManager = LinearLayoutManager(viewRoot.context)
    }

    override fun showNotifications(items: List<Notification>) {
        adapter.setNotifications(items)
    }

}