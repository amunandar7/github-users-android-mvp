package com.github.amunandar7.mvp.architecture

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.eventbus.ApiErrorEvent
import com.github.amunandar7.mvp.util.HttpErrorCode
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

abstract class BaseActivity<P : IPresenter> : AppCompatActivity() {
    @Inject
    lateinit var presenter: P

    private val noInternetConnectionAlert: AlertDialog by lazy {
        AlertDialog.Builder(this).setTitle(R.string.no_internet_title)
            .setMessage(R.string.no_internet_text)
            .setPositiveButton(R.string.close) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
    }

    private val apiLimitExceededAlert: AlertDialog by lazy {
        AlertDialog.Builder(this).setTitle(R.string.api_limit_exceeded_title)
            .setMessage(R.string.api_limit_exceeded_text)
            .setPositiveButton(R.string.close) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this is BaseView) {
            this.onPrepare()
            presenter.attachView(this as BaseView)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        presenter.onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
        presenter.onViewStarted()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
        presenter.onViewStopped()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBaseApiErrorEvent(event: ApiErrorEvent) {
        when (event.code) {
            HttpsURLConnection.HTTP_FORBIDDEN -> apiLimitExceededAlert.show() // API Github limit exceeded
            HttpErrorCode.NO_NETWORK_AVAILABLE -> noInternetConnectionAlert.show()
        }
    }

}