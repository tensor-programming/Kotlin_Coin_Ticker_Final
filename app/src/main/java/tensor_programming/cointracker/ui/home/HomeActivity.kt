package tensor_programming.cointracker.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_settings.*
import kotlinx.android.synthetic.main.dialog_simple.*
import kotlinx.android.synthetic.main.toolbar_home.*
import tensor_programming.cointracker.MainActivity
import tensor_programming.cointracker.R
import tensor_programming.cointracker.base.BaseActivity
import tensor_programming.cointracker.extensions.blockTouchScreen
import tensor_programming.cointracker.extensions.getColorCompat
import tensor_programming.cointracker.extensions.showSnackbar
import tensor_programming.cointracker.extensions.unblockTouchScreen
import tensor_programming.cointracker.model.CurrencyModel
import tensor_programming.cointracker.network.CryptoNetworkService
import tensor_programming.cointracker.network.NetworkUtils
import tensor_programming.cointracker.storage.DelegatedPreferences
import tensor_programming.cointracker.ui.home.adapter.HomeAdapter
import tensor_programming.cointracker.ui.home.detail.DetailDialogFragment
import javax.inject.Inject

/**
 * Created by Tensor on 2/8/2018.
 */
class HomeActivity: BaseActivity(), HomeView, HomeAdapter.HolderListener {

    @Inject
    lateinit var networkUtils: NetworkUtils
    @Inject
    lateinit var service: CryptoNetworkService

    companion object {
        private const val DEFAULT_DURATION = 15L
    }

    private val homePresenter by lazy { HomePresenter(this, service, networkUtils) }
    private var homeAdapter: HomeAdapter? = null
    private var autoUpdateStatus: Boolean by DelegatedPreferences(this,
            DelegatedPreferences.PrefKey.AUTO_UPDATE_STATUS, false)

    private val handler = Handler()

    override fun initInjection() {
        (application as MainActivity).cryptoDeps.inject(this)
    }

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar()
        homeAdapter = HomeAdapter(mutableListOf()).apply {
            listener = this@HomeActivity
        }
        with(rvCrypto) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = homeAdapter
        }
        if (autoUpdateStatus) {
            val timedTask = object : Runnable {
                override fun run() {
                    homePresenter.getAllCurrencies()
                    handler.postDelayed(this, DEFAULT_DURATION * 1000)
                }
            }
            handler.post(timedTask)
        } else {
            homePresenter.getAllCurrencies()
        }
        setupScrollListener()
    }

    private fun setupToolbar() {
        setSupportActionBar((toolbarHome as Toolbar))
        supportActionBar?.let {
            with(it) {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_cross)
                title = ""
            }
            tvToolbarTitle.text = getString(R.string.label_crypto_toolbar)
        }
    }

    private fun setupScrollListener() {
        swipeCrypto?.let {
            it.isEnabled = true
            it.setOnRefreshListener { refreshList() }
            val typedValue = TypedValue()
            this@HomeActivity.theme?.resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize,
                    typedValue, true)
            it.setProgressViewOffset(false, 0,
                    this@HomeActivity.resources.getDimensionPixelSize(typedValue.resourceId))
            it.isRefreshing = true
        }
    }

    override fun disablePullToRefreshProgress() {
        swipeCrypto.isRefreshing = false
    }


    override fun refreshList() {
        (rvCrypto.adapter as HomeAdapter).updateListData(listOf())
        homePresenter.getAllCurrencies()
    }

    override fun updateProgressVisibility(visible: Boolean) {
        swipeCrypto.isRefreshing = visible
        window?.let {
            when {
                visible -> it.blockTouchScreen()
                else -> it.unblockTouchScreen()
            }
        }
    }

    override fun showInternetError() {
        rootHome.showSnackbar(R.string.error_no_internet, timeLength = Snackbar.LENGTH_INDEFINITE) {
            this.setAction(resources.getString(R.string.text_check)) {
                this.dismiss()
                homePresenter.getAllCurrencies()
            }
        }
    }


    override fun updateData(response: List<CurrencyModel>) {
        (rvCrypto.adapter as HomeAdapter).updateListData(response)
    }


    override fun showError(error: Throwable) {
        rootHome.showSnackbar(R.string.error_general)
    }


    override fun onClickHolder(position: Int) {
        val model = (rvCrypto.adapter as HomeAdapter).items[position]
        DetailDialogFragment.newInstance(model).also {
            it.show(supportFragmentManager, it.tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                openSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        generateDialog().let {
            var isAutoOn = false
            with(it.switchAuto) {
                setOnCheckedChangeListener { _, isChecked ->
                    with(it.tvLabelToggle) {
                        isAutoOn = isChecked
                    }
                }
                it.tvLabelToggle.text = getString(R.string.label_auto_refresh,
                        if (isAutoOn) resources.getString(R.string.label_on) else resources.getString(R.string.label_off))

                setTextColor(
                        this@HomeActivity.getColorCompat(if (isChecked) R.color.red else R.color.black)
                )
            }
            with(it) {
                btnSave.setOnClickListener { _ ->
                    dismiss()
                    val refreshIntent = intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    this@HomeActivity.finish()
                    startActivity(refreshIntent)
                    autoUpdateStatus = isAutoOn
                }
                switchAuto.isChecked = autoUpdateStatus
                show()
            }
        }
    }

    private fun generateDialog(): Dialog {
        return this.getTransparentBackgroundDialog().apply {
            setContentView(View.inflate(this@HomeActivity, R.layout.dialog_settings, null))
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
    }

    private fun openExitDialog() {
        val exitDialog = this.getTransparentBackgroundDialog().apply {
            setContentView(View.inflate(this@HomeActivity, R.layout.dialog_simple, null))
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        with(exitDialog) {
            btnNo.setOnClickListener { _ -> this.dismiss() }
            btnYes.setOnClickListener { this@HomeActivity.finish() }
            show()
        }
    }

    override fun setUpLayout() {
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        openExitDialog()
    }

    override fun onDestroy() {
        homePresenter.onDestroy()
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}