package com.github.amunandar7.mvp

import androidx.multidex.MultiDexApplication
import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.di.component.ApplicationComponent
import com.github.amunandar7.mvp.di.component.DaggerApplicationComponent
import com.github.amunandar7.mvp.di.module.ApplicationModule
import com.github.amunandar7.mvp.di.module.GithubApiModule

class BaseApp : MultiDexApplication() {
    lateinit var component: ApplicationComponent
    lateinit var githubApiInterface: GithubApiInterface

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
        if (BuildConfig.DEBUG) {
            // Maybe TimberPlant etc.
        }
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .githubApiModule(GithubApiModule(this))
            .applicationModule(ApplicationModule(this))
            .build()
        component.inject(this)
        githubApiInterface = component.githubApiInterface

    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }


    companion object {
        lateinit var instance: BaseApp private set
    }
}