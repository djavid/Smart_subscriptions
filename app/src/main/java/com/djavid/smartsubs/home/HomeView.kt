package com.djavid.smartsubs.home

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djavid.smartsubs.R
import com.djavid.smartsubs.databinding.FragmentHomeBinding
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.math.roundToInt

class HomeView(
    private val binding: FragmentHomeBinding
) : HomeContract.View {

    private val context = binding.root.context

    private lateinit var presenter: HomeContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>
    private lateinit var adapter: SubsAdapter

    private val itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                presenter.onItemSwipedToLeft(position)
            }
        }
    )

    override fun init(presenter: HomeContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()
        setupRecycler()

        binding.homePeriodSelector.setOnClickListener { presenter.onPeriodPressed() }
        binding.homeAddBtn.setOnClickListener { presenter.onAddSubPressed() }
        binding.homeSortBtn.setOnClickListener { presenter.onSortBtnPressed() }
    }

    override fun showEmptyPlaceholder(show: Boolean) {
        binding.homeEmptyPlaceholder.show(show)
    }

    override fun showProgress(show: Boolean) {
        binding.homeProgressBar.show(show)
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(binding.homeSheetContainer)
    }

    private fun setupRecycler() {
        adapter = SubsAdapter(context, presenter::onItemClick)
        binding.homeSubsRecycler.layoutManager = LinearLayoutManager(context)
        binding.homeSubsRecycler.adapter = adapter
        binding.homeSubsRecycler.setHasFixedSize(true)
        binding.homeSubsRecycler.setItemViewCacheSize(20)
        (binding.homeSubsRecycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        //itemTouchHelper.attachToRecyclerView(viewRoot.home_subsRecycler)
    }

    override fun slidePanelToTop() {
        binding.root.post {
            val offset = context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(binding.root.height - offset, true)
        }
    }

    override fun showSubs(subs: List<Subscription>, pricePeriod: SubscriptionPeriodType) {
        adapter.pricePeriod = pricePeriod
        adapter.showSubs(subs)
    }

    override fun updateListPrices(pricePeriod: SubscriptionPeriodType) {
        adapter.updatePricePeriod(pricePeriod)
    }

    override fun setSubsCount(count: Int) {
        val plural = context.resources.getQuantityString(R.plurals.plural_sub, count)
        binding.homeSubsCount.text = context.getString(R.string.mask_subs_count, count, plural)
    }

    override fun setSubsPrice(price: SubscriptionPrice) {
        val currencySymbol = context.getSymbolForCurrency(price.currency)
        val priceToShow = price.value.roundToInt().toString()

        binding.homeSubsPrice.text = context.getString(R.string.mask_price, priceToShow, currencySymbol)
    }

    override fun setSubsPeriod(period: SubscriptionPeriodType) {
        binding.homePeriodSelector.show(true)
        binding.homePeriodSelector.text = context.getSubPeriodString(period)
    }

}