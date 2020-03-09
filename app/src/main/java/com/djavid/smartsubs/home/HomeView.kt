package com.djavid.smartsubs.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.djavid.smartsubs.R
import com.djavid.smartsubs.models.Subscription
import com.djavid.smartsubs.models.SubscriptionPeriodType
import com.djavid.smartsubs.models.getSubPeriodString
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeView(
    private val viewRoot: View
) : HomeContract.View {

    private lateinit var presenter: HomeContract.Presenter

    override fun init(presenter: HomeContract.Presenter) {
        this.presenter = presenter

        viewRoot.home_periodSelector.setOnClickListener { presenter.onPeriodPressed() }
        viewRoot.home_addBtn.setOnClickListener { presenter.onAddSubPressed(it) }
    }

    override fun showSubs(subs: List<Subscription>) {
        viewRoot.home_subsRecycler.layoutManager = LinearLayoutManager(viewRoot.context)
        viewRoot.home_subsRecycler.adapter = SubsAdapter(viewRoot.context, subs)
    }

    override fun setSubsCount(count: Int) {
        val plural = viewRoot.context.resources.getQuantityString(R.plurals.plural_subs, count)
        viewRoot.home_subsCount.text =
            viewRoot.context.getString(R.string.mask_subs_count, count, plural)
    }

    override fun setSubsPrice(price: Double) {
        viewRoot.home_subsPrice.text = viewRoot.context.getString(R.string.mask_rub, price)
    }

    override fun setSubsPeriod(period: SubscriptionPeriodType) {
        viewRoot.home_periodSelector.text = viewRoot.context.getSubPeriodString(period)
    }

}