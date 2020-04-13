package com.github.amunandar7.mvp.architecture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<P : IPresenter> : AppCompatActivity() {
    @Inject
    lateinit var presenter: P

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
        presenter.onViewStarted()
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewResumed()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewStopped()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

}