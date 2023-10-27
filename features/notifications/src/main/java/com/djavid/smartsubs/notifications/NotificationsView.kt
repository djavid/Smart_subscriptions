package com.djavid.smartsubs.notifications

import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.databinding.FragmentNotificationsBinding
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.subscription.NotificationsAdapter

class NotificationsView(
    private val binding: FragmentNotificationsBinding
) : NotificationsContract.View {

    private val context = binding.root.context

    private lateinit var presenter: NotificationsContract.Presenter
    private lateinit var adapter: NotificationsAdapter

    init {
        binding.notifsAddNotifBtn.setOnClickListener { presenter.onAddNotification() }
    }

    override fun init(presenter: NotificationsContract.Presenter) {
        this.presenter = presenter

        setupNotificationsAdapter()
    }

    private fun setupNotificationsAdapter() {
        adapter = NotificationsAdapter(context, presenter::onEditNotification, presenter::onNotifCheckChanged)
        binding.notifsRecycler.adapter = adapter
        binding.notifsRecycler.layoutManager = LinearLayoutManager(context)
    }

    override fun showNotifications(items: List<Notification>) {
        adapter.setNotifications(items)
    }

}