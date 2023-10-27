package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.common.BasePipeline
import com.djavid.common.CommonFragmentNavigator
import com.djavid.smartsubs.create.CreateContract
import com.djavid.smartsubs.db.NotificationsRepository
import com.djavid.smartsubs.home.HomeNavigator
import com.djavid.data.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPrice
import com.djavid.smartsubs.notifications.NotificationsContract
import com.djavid.data.storage.RealTimeRepository
import com.djavid.common.ACTION_REFRESH
import com.djavid.common.SLIDE_DURATION
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class SubscriptionPresenter(
    private val view: SubscriptionContract.View,
    private val fragmentNavigator: com.djavid.common.CommonFragmentNavigator,
    private val homeNavigator: HomeNavigator,
    private val subscriptionsRepository: com.djavid.data.storage.RealTimeRepository,
    private val notificationsRepository: NotificationsRepository,
    private val modelMapper: com.djavid.data.mappers.SubscriptionModelMapper,
    private val createNavigator: CreateContract.Navigator,
    private val notificationsNavigator: NotificationsContract.Navigator,
    private val logger: FirebaseLogger,
    private val pipeline: com.djavid.common.BasePipeline<Pair<String, String>>,
    coroutineScope: CoroutineScope
) : SubscriptionContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var subscription: Subscription

    private var id: String = ""
    private var isRoot: Boolean = false

    override fun init(id: String?, isRoot: Boolean) {
        view.init(this)
        this.id = id ?: return
        this.isRoot = isRoot

        view.setBackgroundTransparent(false, com.djavid.common.SLIDE_DURATION)
        view.showToolbar(true, com.djavid.common.SLIDE_DURATION)

        listenPipeline()
        reload(true)
    }

    override fun reload(allowCache: Boolean) {
        launch {
            subscription = loadSub(id, allowCache) ?: return@launch
            val notifsCount = notificationsRepository.getNotificationsBySubId(id).count()

            showContent(notifsCount)
        }
    }

    private fun listenPipeline() {
        launch {
            pipeline.getFlow().onEach {
                when (it.first) {
                    com.djavid.common.ACTION_REFRESH -> reload(false)
                }
            }.collect()
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
        view.setSubLogo(subscription.logoBytes)

        //notifs
        view.showNotifsSection(false) //too much bugs, better to make notifs through server
        view.showNotifsSection(subscription.progress != null)
        if (subscription.progress != null) {
            view.setNotifsCount(notifsCount)
        }
    }

    override fun onNotifsClicked() {
        notificationsNavigator.showNotificationsDialog(id)
        launch { logger.onNotifsClicked() }
    }

    private suspend fun loadSub(id: String, allowCache: Boolean): Subscription? =
        withContext(Dispatchers.IO) {
            subscriptionsRepository.getSubById(id, allowCache)?.let {
                modelMapper.fromDao(it)
            }
        }

    override fun onEditClicked() {
        createNavigator.goToCreateScreen(subscription.id)
        launch { logger.onSubEditClicked() }
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
            view.showToolbar(false, com.djavid.common.SLIDE_DURATION)
            view.setBackgroundTransparent(true, com.djavid.common.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(com.djavid.common.SLIDE_DURATION) }
            pipeline.postValue(com.djavid.common.ACTION_REFRESH to "")

            fragmentNavigator.goBack()

            if (isRoot) {
                homeNavigator.goToHome()
            }
        }
    }

}