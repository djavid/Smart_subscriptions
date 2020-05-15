package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPrice
import com.djavid.smartsubs.notification.NotificationContract
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.coroutines.*

class SubscriptionPresenter(
    private val view: SubscriptionContract.View,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val notificationsRepository: NotificationsRepository,
    private val modelMapper: SubscriptionModelMapper,
    private val createNavigator: CreateContract.Navigator,
    private val notificationNavigator: NotificationContract.Navigator,
    coroutineScope: CoroutineScope
) : SubscriptionContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var subscription: Subscription
    private var id: Long = 0

    override fun init(id: Long) {
        view.init(this)
        this.id = id

        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)

        reload()
    }

    override fun reload() {
        launch {
            subscription = loadSub(id) ?: return@launch
            showContent()

            if (subscription.progress != null) {
                val notifications = loadNotifications(id).sortedBy { it.daysBefore }
                view.showNotifications(notifications)
            }
        }
    }

    override fun onAddNotification() {
        notificationNavigator.showNotificationDialog(subscription.id)
    }

    override fun onEditNotification(model: Notification) {
        notificationNavigator.showNotificationDialog(subscription.id, model.id)
    }

    override fun onNotifCheckChanged(notif: Notification, checked: Boolean) {
        launch {
            notificationsRepository.getNotificationById(notif.id)?.let {
                notificationsRepository.editNotification(it.copy(isActive = checked))
            }
        }
    }

    private fun showContent() {
        view.expandPanel(subscription.category != null)
        view.setTitle(subscription.title)
        subscription.category?.let {
            view.setCategory(it)
        }
        view.setPrice(subscription.period, subscription.price)
        subscription.comment?.let {
            view.setComment(it)
        }
        subscription.progress?.let {
            view.setNextPayment(it)
        }
        subscription.overallSpent?.let {
            view.setOverallSpent(SubscriptionPrice(it, subscription.price.currency))
        }
    }

    private suspend fun loadSub(id: Long): Subscription? {
        return subscriptionsRepository.getSubById(id)?.let {
            modelMapper.fromDao(it)
        }
    }

    private suspend fun loadNotifications(id: Long): List<Notification> {
        return notificationsRepository.getNotificationsBySubId(id)
    }

    override fun onEditClicked() {
        createNavigator.goToCreateScreen(subscription.id)
    }

    override fun onDeleteClicked() {
        launch {
            subscriptionsRepository.deleteSubById(subscription.id)
            finish()
        }
    }

    override fun onCloseBtnClicked() {
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun finish() {
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, SLIDE_DURATION)
            view.setBackgroundTransparent(true, SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(SLIDE_DURATION) }
            view.notifyToRefresh()
            fragmentNavigator.goBack()
        }
    }

}