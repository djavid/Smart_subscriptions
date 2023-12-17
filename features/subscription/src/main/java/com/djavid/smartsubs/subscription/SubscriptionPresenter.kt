package com.djavid.smartsubs.subscription

import com.djavid.smartsubs.analytics.FirebaseLogger
import com.djavid.smartsubs.common.base.BasePipeline
import com.djavid.smartsubs.common.navigation.CommonFragmentNavigator
import com.djavid.smartsubs.common.domain.SubscriptionUIModel
import com.djavid.smartsubs.common.domain.SubscriptionPrice
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

    private lateinit var subscriptionUIModel: SubscriptionUIModel

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
            subscriptionUIModel = loadSub(id, allowCache) ?: return@launch
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
        view.expandPanel(subscriptionUIModel.category != null)
        view.setTitle(subscriptionUIModel.title)
        subscriptionUIModel.category?.let {
            view.setCategory(it)
        }
        view.setPrice(subscriptionUIModel.period, subscriptionUIModel.price)
        subscriptionUIModel.comment?.let {
            view.setComment(it)
        }
        subscriptionUIModel.progress?.let {
            view.setNextPayment(it)
        }
        subscriptionUIModel.overallSpent?.let {
            view.setOverallSpent(SubscriptionPrice(it, subscriptionUIModel.price.currency))
        }
        view.setSubLogo(subscriptionUIModel.logoUrl)

        //notifs
        view.showNotifsSection(false) //todo AlarmManager or WorkManager release 1.1-1.2
        //view.showNotifsSection(subscription.progress != null)
        if (subscriptionUIModel.progress != null) {
            view.setNotifsCount(notifsCount)
        }
    }

    override fun onNotifsClicked() {
        notificationsNavigator.showNotificationsDialog(id)
        launch { logger.onNotifsClicked() }
    }

    private suspend fun loadSub(id: String, allowCache: Boolean): SubscriptionUIModel? =
        withContext(Dispatchers.IO) {
            subscriptionsRepository.getSubById(id, allowCache)?.let {
                modelMapper.fromDao(it)
            }
        }

    override fun onEditClicked() {
        createNavigator.goToCreateScreen(subscriptionUIModel.id)
        launch { logger.onSubEditClicked() }
    }

    override fun onDeleteClicked() {
        view.showDeletionPromptDialog()
    }

    override fun onDeletionPrompted() {
        launch {
            subscriptionsRepository.deleteSubById(subscriptionUIModel.id)
            logger.subDelete(subscriptionUIModel)
            finish()
        }
    }

    override fun goBack() {
        finish()
    }

    private fun finish() {
        launch {
            view.hideKeyboard()
            view.collapsePanel()
            view.showToolbar(false, Constants.SLIDE_DURATION)
            view.setBackgroundTransparent(true, Constants.SLIDE_DURATION)
            delay(Constants.SLIDE_DURATION)
            pipeline.postValue(Constants.ACTION_REFRESH to "")

            fragmentNavigator.goBack()

            if (isRoot) {
                homeNavigator.goToHome()
            }
        }
    }
}