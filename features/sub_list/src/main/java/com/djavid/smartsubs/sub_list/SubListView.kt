package com.djavid.smartsubs.sub_list

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.djavid.core.ui.R
import com.djavid.core.ui.databinding.FragmentSubListBinding
import com.djavid.smartsubs.common.utils.Constants
import com.djavid.smartsubs.common.utils.animateAlpha
import com.djavid.smartsubs.common.utils.hideKeyboard
import com.djavid.smartsubs.common.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SubListView(
    private var _binding: FragmentSubListBinding?,
    private val viewModel: SubListViewModel,
    private val coroutineScope: LifecycleCoroutineScope
) : SubListContract.View {

    private val binding get() = requireNotNull(_binding)

    private lateinit var presenter: SubListContract.Presenter
    private val bottomSheet: BottomSheetBehavior<FrameLayout>
        get() = BottomSheetBehavior.from(binding.subListBottomSheet)
    private val context = binding.root.context

    override fun init(presenter: SubListContract.Presenter) {
        this.presenter = presenter

        setBackgroundTransparent(false, Constants.SLIDE_DURATION)
        showToolbar(true, Constants.SLIDE_DURATION)
        expandPanel()

        setupListeners()
        setupObservers()
    }

    override fun destroy() {
    }

    private fun setupListeners() {
        binding.subListRecycler.adapter = SubsListAdapter(presenter::onItemClick)
        binding.subListCloseBtn.setOnClickListener { presenter.onBackPressed() }
    }

    private fun setupObservers() {
        viewModel.predefinedSubsFlow.onEach {
            showProgress(false)
            (binding.subListRecycler.adapter as? SubsListAdapter)?.showSubs(it)
        }.launchIn(coroutineScope)
    }

    override fun showToolbar(show: Boolean, duration: Long) {
        val fromAlpha = if (show) 0f else 1f
        val toAlpha = if (show) 1f else 0f
        binding.subListToolbar.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun setBackgroundTransparent(transparent: Boolean, duration: Long) {
        val fromAlpha = if (transparent) 1f else 0f
        val toAlpha = if (transparent) 0f else 1f
        binding.root.animateAlpha(fromAlpha, toAlpha, duration)
    }

    override fun hideKeyboard() {
        (context as? AppCompatActivity).hideKeyboard()
    }

    override fun collapsePanel() {
        binding.root.post {
            bottomSheet.setPeekHeight(0, true)
        }
    }

    override fun expandPanel() {
        binding.root.post {
            val offset = context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

    override fun showProgress(show: Boolean) {
        binding.subListProgress.show(show)
    }
}