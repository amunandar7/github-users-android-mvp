package com.github.amunandar7.mvp.ui.main

import android.os.Bundle
import android.util.Log
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.architecture.BaseActivity
import com.github.amunandar7.mvp.di.component.ApplicationComponent
import com.github.amunandar7.mvp.di.component.DaggerMainComponent
import com.github.amunandar7.mvp.di.module.MainModule
import com.github.amunandar7.mvp.model.UserModel
import javax.inject.Inject


class MainActivity : BaseActivity<MainContract.Presenter>(), MainContract.View {
    @Inject
    lateinit var githubApiInterface: GithubApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPrepare() {
        super.onPrepare()
        Log.d("17417", "onPrepare")
        val applicationComponent: ApplicationComponent =
            BaseApp.instance.getApplicationComponent()
        val activityComponent = DaggerMainComponent.builder()
            .applicationComponent(applicationComponent)
            .mainModule(MainModule(this)).build()
        activityComponent.inject(this)

    }


    override fun initializeView() {
        Log.d("17417", "initializeView")

//        githubApiInterface.users.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread()).subscribe({
//                onLoadUserDataSuccess(it)
//            }, {
//                onLoadUserDataFailed(it)
//            })
    }

    override fun onLoadUserDataSuccess(users: List<UserModel>) {
        Log.d("17417", "onLoadUserDataSuccess users count = ${users.size}")

    }

    override fun onLoadUserDataFailed(throwable: Throwable) {
        Log.d("17417", "onLoadUserDataFailed ${throwable.message}")
        throwable.printStackTrace()
    }

}
