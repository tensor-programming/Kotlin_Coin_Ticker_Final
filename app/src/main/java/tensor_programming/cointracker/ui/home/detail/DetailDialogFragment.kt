package tensor_programming.cointracker.ui.home.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.detail_dialog.*
import tensor_programming.cointracker.R
import tensor_programming.cointracker.extensions.*
import tensor_programming.cointracker.model.CurrencyModel

/**
 * Created by Tensor on 2/9/2018.
 */
class DetailDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(model: CurrencyModel): DetailDialogFragment {
            val args = Bundle().apply {
                putParcelable(CURRENCY_MODEL, model)
            }
            return DetailDialogFragment().apply {
                arguments = args
            }
        }
        const val CURRENCY_MODEL = "DetailDialogFragment.CURRENCY_MODEL"
        private const val BOTTOM_SHEET_HEIGHT = 320
        private const val MINUS = "-"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.detail_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val model = it.getParcelable<CurrencyModel>(CURRENCY_MODEL)
            updateDisplayDetail(model)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDisplayDetail(model: CurrencyModel) {
        tvName.text = model.name
        tvCode.text = "(${model.symbol})"
        tvPrice.text = "$${model.priceUsd}"

        model.dayPercentChange?.let {
            tvStatsDaily.text = "$it %"
            val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
            ivIndicatorDaily.setImageDrawable(context.getDrawableCompat(arrowStats))
        }
        model.weekPercentChange?.let {
            tvStatsWeekly.text = "$it %"
            val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
            ivIndicatorWeekly.setImageDrawable(context.getDrawableCompat(arrowStats))
        }
        model.hourPercentChange?.let {
            tvStatsHourly.text = "$it %"
            val arrowStats = if (it.contains(MINUS)) R.drawable.arrow_down else R.drawable.arrow_up
            ivIndicatorHourly.setImageDrawable(context.getDrawableCompat(arrowStats))
        }
        model.lastUpdated?.let {
            val unixTimestamp = it.safeToLong()
            tvLastUpdate.text = unixTimestamp.unixToDate()
        }

        with(tvDailyVolumeUsdValue) {
            val volume = model.dailyVolumeUsd?.trimTrailingZeros()
            text = "$$volume"
        }

        with(tvMarketCapUsdValue) {
            val marketCap = model.marketCapUsd?.trimTrailingZeros()
            text = "$$marketCap"
        }

        model.availableSupply?.let {
            val formatted = it.trimTrailingZeros()
            tvAvailableSupplyValue.text = formatted
        }

        model.totalSupply?.let {
            val formatted = it.trimTrailingZeros()
            tvTotalSupplyValue.text = formatted
        }

        model.maxSupply?.let {
            val formatted = it.trimTrailingZeros()
            tvMaxSupplyValue.text = formatted
        }

        model.priceIdr?.let {
            val formatted = it.trimTrailingZeros()
            tvPriceIdrValue.text = "Rp.$formatted"
        }

        model.dailyVolumeIdr?.let {
            val volume = it.trimTrailingZeros()
            tvDailyVolumeIdrValue.text = "Rp.$volume"
        }

        model.marketCapIdr?.let {
            val marketCap = it.trimTrailingZeros()
            tvMarketCapIdrValue.text = "Rp.$marketCap"
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { curDialog ->
            val bottomSheet = curDialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = context.convertDpToPx(BOTTOM_SHEET_HEIGHT)
        }
        val curView = view
        curView?.let {
            it.post {
                val parent = it.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<View>?

                bottomSheetBehavior?.let { behave ->
                    behave.peekHeight = it.measuredHeight
                }
                parent.setBackgroundColor(context.getColorCompat(R.color.colorAccent))
            }
        }
    }

}