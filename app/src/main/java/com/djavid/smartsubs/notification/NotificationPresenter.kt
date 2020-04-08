package com.djavid.smartsubs.notification

import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.models.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.LocalTime

class NotificationPresenter(
    private val view: NotificationContract.View,
    private val repository: NotificationsRepository,
    coroutineScope: CoroutineScope
) : NotificationContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var model: Notification
    private var editMode = false
    private var timeChosen = false

    override fun init(subscriptionId: Long, id: Long?) {
        view.init(this)

        launch {
            model = Notification(0, subscriptionId, false, -1, LocalTime(), false)

            if (id != null) {
                repository.getNotificationById(id)?.let { model = it }
                editMode = true
                timeChosen = true
            }
        }
    }

}