package com.djavid.smartsubs.home

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.*
import com.djavid.smartsubs.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.math.roundToInt

class HomeView(
    private val viewRoot: View
) : HomeContract.View {

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

        viewRoot.home_periodSelector.setOnClickListener { presenter.onPeriodPressed() }
        viewRoot.home_addBtn.setOnClickListener { presenter.onAddSubPressed() }
        viewRoot.home_sortBtn.setOnClickListener { presenter.onSortBtnPressed() }
    }

    override fun showEmptyPlaceholder(show: Boolean) {
        viewRoot.home_emptyPlaceholder.show(show)
    }

    override fun showProgress(show: Boolean) {
        viewRoot.home_progressBar.show(show)
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.home_sheetContainer)
    }

    private fun setupRecycler() {
        adapter = SubsAdapter(viewRoot.context, presenter::onItemClick)
        viewRoot.home_subsRecycler.layoutManager = LinearLayoutManager(viewRoot.context)
        viewRoot.home_subsRecycler.adapter = adapter
        viewRoot.home_subsRecycler.setHasFixedSize(true)
        viewRoot.home_subsRecycler.setItemViewCacheSize(20)
        (viewRoot.home_subsRecycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        //itemTouchHelper.attachToRecyclerView(viewRoot.home_subsRecycler)
    }

    override fun slidePanelToTop() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
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
        val plural = viewRoot.context.resources.getQuantityString(R.plurals.plural_sub, count)
        viewRoot.home_subsCount.text = viewRoot.context.getString(R.string.mask_subs_count, count, plural)
    }

    override fun setSubsPrice(price: SubscriptionPrice) {
        val currencySymbol = viewRoot.context.getSymbolForCurrency(price.currency)
        val priceToShow = price.value.roundToInt().toString()

        viewRoot.home_subsPrice.text = viewRoot.context.getString(R.string.mask_price, priceToShow, currencySymbol)
    }

    override fun setSubsPeriod(period: SubscriptionPeriodType) {
        viewRoot.home_periodSelector.show(true)
        viewRoot.home_periodSelector.text = viewRoot.context.getSubPeriodString(period)
    }

}