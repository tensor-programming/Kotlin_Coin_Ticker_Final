package tensor_programming.cointracker.ui.splash

import tensor_programming.cointracker.network.NetworkUtils

/**
 * Created by Tensor on 2/8/2018.
 */
class SplashPresenter(private val splashView: SplashView, private val networkUtils: NetworkUtils) {
    fun checkRequirement() {
        when {
            networkUtils.isConnected -> {
                splashView.navigateToHome()
            }
            else -> splashView.showInternetError()
        }
    }

}