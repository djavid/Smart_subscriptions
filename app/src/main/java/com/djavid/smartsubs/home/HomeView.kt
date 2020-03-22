package com.djavid.smartsubs.home

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getSubPeriodString
import com.djavid.smartsubs.models.getSymbolForCurrency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.DecimalFormat
import java.util.*

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
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.home_sheetContainer)
    }

    private fun setupRecycler() {
        adapter = SubsAdapter(viewRoot.context)
        viewRoot.home_subsRecycler.layoutManager = LinearLayoutManager(viewRoot.context)
        viewRoot.home_subsRecycler.adapter = adapter
        itemTouchHelper.attachToRecyclerView(viewRoot.home_subsRecycler)
    }

    override fun slidePanelToTop() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
        }
    }

    override fun showSubs(subs: List<Subscription>) {
        adapter.showSubs(subs)
    }

    override fun setSubsCount(count: Int) {
        val plural = viewRoot.context.resources.getQuantityString(R.plurals.plural_sub, count)
        viewRoot.home_subsCount.text = viewRoot.context.getString(R.string.mask_subs_count, count, plural)
    }

    override fun setSubsPrice(price: Double, currency: Currency) {
        val currencySymbol = viewRoot.context.getSymbolForCurrency(currency)
        val df = DecimalFormat("0.##")

        viewRoot.home_subsPrice.text = viewRoot.context.getString(R.string.mask_price, df.format(price), currencySymbol)
    }

    override fun setSubsPeriod(period: SubscriptionPeriodType) {
        viewRoot.home_periodSelector.text = viewRoot.context.getSubPeriodString(period)
    }

}