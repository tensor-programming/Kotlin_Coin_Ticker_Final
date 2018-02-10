package tensor_programming.cointracker.ui.home

import tensor_programming.cointracker.model.CurrencyModel

/**
 * Created by Tensor on 2/9/2018.
 */
interface HomeView {
    fun updateProgressVisibility(visible: Boolean)

    fun showInternetError()

    fun updateData(response: List<CurrencyModel>)

    fun showError(error: Throwable)

    fun disablePullToRefreshProgress()

    fun refreshList()
}