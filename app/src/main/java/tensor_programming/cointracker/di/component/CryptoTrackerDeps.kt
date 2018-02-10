package tensor_programming.cointracker.di.component

import dagger.Component
import tensor_programming.cointracker.MainActivity
import tensor_programming.cointracker.di.module.AppModule
import tensor_programming.cointracker.di.module.CryptoNetworkModule
import tensor_programming.cointracker.ui.home.HomeActivity
import tensor_programming.cointracker.ui.splash.SplashActivity
import javax.inject.Singleton

/**
 * Created by Tensor on 2/7/2018.
 */

@Singleton
@Component(modules = [
    (AppModule::class),
    (CryptoNetworkModule::class)
])
interface CryptoTrackerDeps {
    fun inject(mainActivity: MainActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(homeActivity: HomeActivity)
}