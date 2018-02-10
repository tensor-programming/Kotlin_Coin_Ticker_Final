package tensor_programming.cointracker.ui.splash

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.splash_layout.*
import org.jetbrains.anko.act
import org.jetbrains.anko.startActivity
import tensor_programming.cointracker.MainActivity
import tensor_programming.cointracker.R
import tensor_programming.cointracker.base.BaseActivity
import tensor_programming.cointracker.extensions.showSnackbar
import tensor_programming.cointracker.network.NetworkUtils
import tensor_programming.cointracker.ui.home.HomeActivity
import javax.inject.Inject

/**
 * Created by Tensor on 2/8/2018.
 */
class SplashActivity: BaseActivity(), SplashView {


    @Inject
    lateinit var networkUtils: NetworkUtils

    private val splashPresenter by lazy { SplashPresenter(this, networkUtils) }

    override fun initInjection() {
        (application as MainActivity).cryptoDeps.inject(this)
    }

    override fun navigateToHome() {
        Handler().postDelayed({
            startActivity<HomeActivity>()
            this@SplashActivity.finish()
        }, 3000)
    }

    override fun showInternetError() {
        rootSplash.showSnackbar(R.string.error_no_internet, timeLength = Snackbar.LENGTH_INDEFINITE) {
            this.setAction(resources.getString(R.string.text_check)) {
                this.dismiss()
                act.recreate()
            }
        }
    }

    override fun setUpLayout() {
        setContentView(R.layout.splash_layout)
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        splashPresenter.checkRequirement()
    }

}