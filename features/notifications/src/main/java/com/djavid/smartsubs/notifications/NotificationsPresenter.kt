package com.djavid.smartsubs.notifications

import com.djavid.common.BasePipeline
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.notification.AlarmNotifier
import com.djavid.smartsubs.notification.NotificationContract
import com.djavid.common.ACTION_REFRESH
import com.djavid.smartsubs.analytics.FirebaseLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NotificationsPresenter(
    private val view: NotificationsContract.View,
    private val notificationNavigator: com.djavid.smartsubs.notification.NotificationContract.Navigator,
    private val alarmNotifier: com.djavid.smartsubs.notification.AlarmNotifier,
    private val notificationsRepository: NotificationsRepository,
    private val pipeline: com.djavid.common.BasePipeline<Pair<String, String>>,
    private val logger: FirebaseLogger,
    coroutineScope: CoroutineScope
) : NotificationsContract.Presenter, CoroutineScope by coroutineScope {

    private var subId: String = ""

    override fun init(subId: String?) {
        view.init(this)
        this.subId = subId ?: ""

        listenPipeline()
        reloadNotifs()
    }

    private fun reloadNotifs() {
        launch {
            val notifications = loadNotifications(this@NotificationsPresenter.subId).sortedBy { it.daysBefore }
            view.showNotifications(notifications)
        }
    }

    private fun listenPipeline() {
        launch {
            pipeline.getFlow().onEach {
                when (it.first) {
                    com.djavid.common.ACTION_REFRESH -> reloadNotifs()
                }
            }.collect()
        }
    }

    private suspend fun loadNotifications(id: String): List<Notification> {
        return notificationsRepository.getNotificationsBySubId(id)
    }

    override fun onAddNotification() {
        launch {
            notificationNavigator.showNotificationDialog(subId)
            logger.onAddNotifClicked()
        }
    }

    override fun onEditNotification(model: Notification) {
        launch {
            notificationNavigator.showNotificationDialog(subId, model.id)
            logger.onEditNotifClicked(model)
        }
    }

    override fun onNotifCheckChanged(notif: Notification, checked: Boolean) {
        launch {
            notificationsRepository.getNotificationById(notif.id)?.let { notif ->
                notificationsRepository.editNotification(notif.copy(isActive = checked))

                if (checked) {
                    alarmNotifier.setAlarm(notif)
                } else {
                    alarmNotifier.cancelAlarm(notif.id)
                }

                logger.onNotifCheckClicked(notif, checked)
            }
        }
    }

}