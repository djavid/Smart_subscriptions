package com.djavid.smartsubs.home

import androidx.lifecycle.LifecycleOwner
import com.djavid.core.ui.R
import com.djavid.features.home.databinding.FragmentHomeBinding
import com.djavid.smartsubs.common.utils.getCurrencySymbol
import com.djavid.smartsubs.common.utils.show
import com.djavid.ui.getSubPeriodString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.orbitmvi.orbit.viewmodel.observe
import kotlin.math.roundToInt

class HomeView(
    private val binding: FragmentHomeBinding,
    private val viewModel: HomeViewModel
) {

    private val context = binding.root.context

    private lateinit var adapter: SubsAdapter

    fun onViewCreated(viewLifecycleOwner: LifecycleOwner) {
        setupRecycler()

        viewModel.observe(
            state = ::render,
            sideEffect = ::handleSideEffect,
            lifecycleOwner = viewLifecycleOwner
        )

        binding.homePeriodSelector.setOnClickListener { viewModel.onPeriodPressed() }
        binding.homeAddBtn.setOnClickListener { viewModel.onAddSubPressed() }
        binding.homeSortBtn.setOnClickListener { viewModel.onSortBtnPressed() }
    }

    private fun handleSideEffect(sideEffect: HomeSideEffect) {
        when (sideEffect) {
            is HomeSideEffect.SlidePanelToTop -> slidePanelToTop()
        }
    }

    private fun render(state: HomeState) {
        binding.homeEmptyPlaceholder.show(state.subsList.isEmpty())
        binding.homeSheetProgressBar.show(state.isProgress)
        binding.homeProgressBar.show(state.isProgress)

        adapter.showSubs(state.subsList)
        adapter.updatePricePeriod(state.pricePeriod)

        val currencySymbol = state.price.currency.getCurrencySymbol()
        val priceToShow = state.price.value.roundToInt().toString()
        binding.homeSubsPrice.text = context.getString(R.string.mask_price, priceToShow, currencySymbol)
        binding.homeSubsPrice.show(true)

        val plural = context.resources.getQuantityString(R.plurals.plural_sub, state.subsList.size)
        binding.homeSubsCount.text = context.getString(R.string.mask_subs_count, state.subsList.size, plural)

        binding.homePeriodSelector.text = context.getSubPeriodString(state.pricePeriod)
        binding.homePeriodSelector.show(true)
    }

    private fun setupRecycler() {
        adapter = SubsAdapter(context, viewModel::onItemClick)
        binding.homeSubsRecycler.adapter = adapter
        binding.homeSubsRecycler.setItemViewCacheSize(20)
    }

    private fun slidePanelToTop() {
        binding.root.post {
            val offset = context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            val bottomSheet = BottomSheetBehavior.from(binding.homeSheetContainer)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

}