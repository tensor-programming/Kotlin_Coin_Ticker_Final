package tensor_programming.cointracker.ui.home

import io.reactivex.disposables.CompositeDisposable
import tensor_programming.cointracker.extensions.safeDispose
import tensor_programming.cointracker.network.NetworkUtils
import tensor_programming.cointracker.network.CryptoNetworkService
/**
 * Created by Tensor on 2/9/2018.
 */
class HomePresenter(private val homeView: HomeView,
                    private val service: CryptoNetworkService,
                    private val networkUtils: NetworkUtils) {

    private val disposables = CompositeDisposable()

    fun getAllCurrencies() {
        when {
            networkUtils.isConnected -> {
                homeView.updateProgressVisibility(true)
                disposables.add(service.getAllCurrency(
                        onSuccess = { response ->
                            homeView.disablePullToRefreshProgress()
                            homeView.updateProgressVisibility(false)
                            if (response.isNotEmpty()) {
                                val sortedList = response.sortedWith(compareBy {
                                    it.rank?.toInt()
                                })
                                homeView.updateData(sortedList)
                            }
                        },
                        onError = {
                            homeView.disablePullToRefreshProgress()
                            homeView.updateProgressVisibility(false)
                            homeView.showError(it)
                        }))
            }
            else -> {
                homeView.disablePullToRefreshProgress()
                homeView.showInternetError()
            }
        }
    }

    fun onDestroy() {
        disposables.safeDispose()
    }


}