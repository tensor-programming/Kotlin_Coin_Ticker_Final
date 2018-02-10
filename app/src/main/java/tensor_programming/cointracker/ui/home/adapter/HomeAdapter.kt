package tensor_programming.cointracker.ui.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.coin_item.view.*
import tensor_programming.cointracker.R
import tensor_programming.cointracker.extensions.getDrawableCompat
import tensor_programming.cointracker.extensions.safeToLong
import tensor_programming.cointracker.extensions.unixToDate
import tensor_programming.cointracker.model.CurrencyModel

/**
 * Created by Tensor on 2/9/2018.
 */
class HomeAdapter(val items: MutableList<CurrencyModel>) :
        RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var listener: HolderListener? = null

    companion object {
        const val MINUS = "-"
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder {
        val rootView = LayoutInflater.from(parent?.context).inflate(R.layout.coin_item,
                parent, false)
        return HomeViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder?, position: Int) {
        holder?.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    fun updateListData(newItems: List<CurrencyModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }



    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CurrencyModel, listener: HolderListener?) {
            with(itemView) {
                tvCode.text = "(${item.symbol})"
                tvName.text = item.name
                tvPrice.text = "$${item.priceUsd}"
                item.dayPercentChange?.let {
                    tvStatsDaily.text = "$it %"
                    val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
                    ivIndicatorDaily.setImageDrawable(itemView.context.getDrawableCompat(arrowStats))
                }
                item.weekPercentChange?.let {
                    tvStatsWeekly.text = "$it %"
                    val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
                    ivIndicatorWeekly.setImageDrawable(itemView.context.getDrawableCompat(arrowStats))
                }
                item.hourPercentChange?.let {
                    tvStatsHourly.text = "$it %"
                    val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
                    ivIndicatorHourly.setImageDrawable(itemView.context.getDrawableCompat(arrowStats))
                }
                item.lastUpdated?.let {
                    val unixTimestamp = it.safeToLong()
                    tvLastUpdate.text = unixTimestamp.unixToDate()
                }
                setOnClickListener { listener?.onClickHolder(adapterPosition) }
            }

        }

    }

    interface HolderListener {
        fun onClickHolder(position: Int)
    }
}
