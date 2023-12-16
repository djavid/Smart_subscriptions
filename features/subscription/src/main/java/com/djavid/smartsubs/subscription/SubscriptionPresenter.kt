package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import com.djavid.smartsubs.common.models.Subscription
import com.djavid.smartsubs.common.models.SubscriptionPrice
import com.djavid.smartsubs.common.navigation.CreateNavigator
import com.djavid.smartsubs.common.navigation.HomeNavigator
import com.djavid.smartsubs.common.navigation.NotificationsNavigator
import com.djavid.smartsubs.data.db.NotificationsRepository
import com.djavid.smartsubs.data.mappers.SubscriptionModelMapper
import com.djavid.smartsubs.data.storage.RealTimeRepository
import com.djavid.smartsubs.common.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubscriptionPresenter(
    private val view: SubscriptionContract.View,
    private val fragmentNavigator: CommonFragmentNavigator,
    private val homeNavigator: HomeNavigator,
    private val subscriptionsRepository: RealTimeRepository,
    private val notificationsRepository: NotificationsRepository,
    private val modelMapper: SubscriptionModelMapper,
    private val createNavigator: CreateNavigator,
    private val notificationsNavigator: NotificationsNavigator,
    private val logger: FirebaseLogger,
    private val pipeline: BasePipeline<Pair<String, String>>,
    coroutineScope: CoroutineScope
) : SubscriptionContract.Presenter, CoroutineScope by coroutineScope {

    private lateinit var subscription: Subscription

    private var id: String = ""
    private var isRoot: Boolean = false

    override fun init(id: String?, isRoot: Boolean) {
        view.init(this)
        this.id = id ?: return
        this.isRoot = isRoot

        view.setBackgroundTransparent(false, Constants.SLIDE_DURATION)
        view.showToolbar(true, Constants.SLIDE_DURATION)

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
        pipeline.getFlow().onEach {
            when (it.first) {
                Constants.ACTION_REFRESH -> reload(false)
            }
        }.launchIn(this)
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
        view.setSubLogo(subscription.logoUrl)

        //notifs
        view.showNotifsSection(false) //todo AlarmManager or WorkManager release 1.1-1.2
        //view.showNotifsSection(subscription.progress != null)
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

    private fun finish() { //todo шо за жесть
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, Constants.SLIDE_DURATION)
            view.setBackgroundTransparent(true, Constants.SLIDE_DURATION)
            withContext(Dispatchers.Default) { delay(Constants.SLIDE_DURATION) }
            pipeline.postValue(Constants.ACTION_REFRESH to "")

            fragmentNavigator.goBack()

            if (isRoot) {
                homeNavigator.goToHome()
            }
        }
    }
}