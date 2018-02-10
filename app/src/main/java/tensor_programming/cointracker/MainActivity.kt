package tensor_programming.cointracker

import android.annotation.SuppressLint
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import tensor_programming.cointracker.di.component.CryptoTrackerDeps
import tensor_programming.cointracker.di.component.DaggerCryptoTrackerDeps
import tensor_programming.cointracker.di.module.AppModule

@SuppressLint("Registered")
class MainActivity : Application() {

    lateinit var cryptoDeps: CryptoTrackerDeps

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        cryptoDeps = DaggerCryptoTrackerDeps.builder()
                .appModule(AppModule(this))
                .build()
        cryptoDeps.inject(this)
    }
}
