package com.djavid.smartsubs.home

import androidx.lifecycle.LifecycleOwner
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentHomeBinding
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.getSymbolString
import com.djavid.smartsubs.utils.show
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
        binding.homeProgressBar.show(state.isProgress)

        adapter.showSubs(state.subsList)
        adapter.updatePricePeriod(state.pricePeriod)

        val currencySymbol = state.price.currency.getSymbolString()
        val priceToShow = state.price.value.roundToInt().toString()
        binding.homeSubsPrice.text = context.getString(R.string.mask_price, priceToShow, currencySymbol)

        val plural = context.resources.getQuantityString(R.plurals.plural_sub, state.subsList.size)
        binding.homeSubsCount.text = context.getString(R.string.mask_subs_count, state.subsList.size, plural)

        binding.homePeriodSelector.text = context.getSubPeriodString(state.pricePeriod)
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