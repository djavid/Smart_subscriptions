package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.common.BasePipeline
import com.djavid.smartsubs.common.CommonFragmentNavigator
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.db.SubscriptionsRepository
import com.djavid.smartsubs.home.HomeContract
import com.djavid.smartsubs.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Notification
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPrice
import com.djavid.smartsubs.notification.AlarmNotifier
import com.djavid.smartsubs.notification.NotificationContract
import com.djavid.smartsubs.notifications.NotificationsContract
import com.djavid.smartsubs.utils.ACTION_REFRESH
import com.djavid.smartsubs.utils.FirebaseLogger
import com.djavid.smartsubs.utils.SLIDE_DURATION
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach

class SubscriptionPresenter(
    private val view: SubscriptionContract.View,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val homeNavigator: HomeContract.Navigator,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val notificationsRepository: NotificationsRepository,
    private val modelMapper: SubscriptionModelMapper,
    private val createNavigator: CreateContract.Navigator,
    private val notificationsNavigator: NotificationsContract.Navigator,
    private val logger: FirebaseLogger,
    private val pipeline: BasePipeline<Pair<String, String>>,
    coroutineScope: CoroutineScope
) : SubscriptionContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var channel: ReceiveChannel<Pair<String, String>>
    private lateinit var subscription: Subscription
    private var id: Long = 0
    private var isRoot: Boolean = false

    override fun init(id: Long, isRoot: Boolean) {
        view.init(this)
        this.id = id
        this.isRoot = isRoot

        view.setBackgroundTransparent(false, SLIDE_DURATION)
        view.showToolbar(true, SLIDE_DURATION)

        listenPipeline()
        reload()
    }

    override fun reload() {
        launch {
            subscription = loadSub(id) ?: return@launch
            val notifsCount = notificationsRepository.getNotificationsBySubId(id).count()

            showContent(notifsCount)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun listenPipeline() {
        launch {
            channel = pipeline.subscribe()
            channel.consumeEach {
                when (it.first) {
                    ACTION_REFRESH -> reload()
                }
            }
        }
    }

    private fun showContent(notifsCount: Int) {
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
        view.setNotifsCount(notifsCount)
    }

    override fun onNotifsClicked() {
        notificationsNavigator.showNotificationsDialog(id)
    }

    private suspend fun loadSub(id: Long): Subscription? {
        return subscriptionsRepository.getSubById(id)?.let {
            modelMapper.fromDao(it)
        }
    }

    override fun onEditClicked() {
        createNavigator.goToCreateScreen(subscription.id)
    }

    override fun onDeleteClicked() {
        view.showDeletionPromptDialog()
    }

    override fun onDeletionPrompted() {
        launch {
            subscriptionsRepository.deleteSubById(subscription.id)
            logger.subDelete(subscription)
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

            if (isRoot) {
                homeNavigator.goToHome()
            }
        }
    }

}