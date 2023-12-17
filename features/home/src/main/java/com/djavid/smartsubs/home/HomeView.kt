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
    private var _binding: FragmentHomeBinding? = null,
    private val viewModel: HomeViewModel
) {

    private val binding get() = requireNotNull(_binding)

    fun onViewCreate(viewLifecycleOwner: LifecycleOwner) {
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

    fun onViewDestroy() {
        _binding = null
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

        (binding.homeSubsRecycler.adapter as? SubsAdapter)?.let { adapter ->
            adapter.showSubs(state.subsList)
            adapter.updatePricePeriod(state.pricePeriod)
        }

        val currencySymbol = state.price.currency.getCurrencySymbol()
        val priceToShow = state.price.value.roundToInt().toString()
        binding.homeSubsPrice.text = binding.root.context.getString(R.string.mask_price, priceToShow, currencySymbol)
        binding.homeSubsPrice.show(true)

        val plural = binding.root.context.resources.getQuantityString(R.plurals.plural_sub, state.subsList.size)
        binding.homeSubsCount.text = binding.root.context.getString(R.string.mask_subs_count, state.subsList.size, plural)

        binding.homePeriodSelector.text = binding.root.context.getSubPeriodString(state.pricePeriod)
        binding.homePeriodSelector.show(true)
    }

    private fun setupRecycler() {
        binding.homeSubsRecycler.adapter = SubsAdapter(binding.root.context, viewModel::onItemClick)
        binding.homeSubsRecycler.setItemViewCacheSize(20)
    }

    private fun slidePanelToTop() {
        binding.root.post {
            val offset = binding.root.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            val bottomSheet = BottomSheetBehavior.from(binding.homeSheetContainer)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }
}