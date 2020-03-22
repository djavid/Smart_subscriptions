package com.djavid.smartsubs.home

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.DecimalFormat

class HomeView(
    private val viewRoot: View
) : HomeContract.View {

    private lateinit var presenter: HomeContract.Presenter
    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    override fun init(presenter: HomeContract.Presenter) {
        this.presenter = presenter
        setupBottomSheet()

        viewRoot.home_periodSelector.setOnClickListener { presenter.onPeriodPressed() }
        viewRoot.home_addBtn.setOnClickListener { presenter.onAddSubPressed() }
    }

    private fun setupBottomSheet() {
        bottomSheet = BottomSheetBehavior.from(viewRoot.home_sheetContainer)
    }

    override fun slidePanelToTop() {
        viewRoot.post {
            val offset = viewRoot.context.resources.getDimensionPixelOffset(R.dimen.toolbar_height)
            bottomSheet.setPeekHeight(viewRoot.height - offset, true)
        }
    }

    override fun showSubs(subs: List<Subscription>) {
        viewRoot.home_subsRecycler.layoutManager = LinearLayoutManager(viewRoot.context)
        viewRoot.home_subsRecycler.adapter = SubsAdapter(viewRoot.context, subs)
    }

    override fun setSubsCount(count: Int) {
        val plural = viewRoot.context.resources.getQuantityString(R.plurals.plural_subs, count)
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