package com.github.amunandar7.mvp.di.module

import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.main.MainActivity
import com.github.amunandar7.mvp.ui.main.MainContract
import com.github.amunandar7.mvp.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule(
    var activity: MainActivity
) {

    @Provides
    @ActivityScope
    fun provideActivity(): MainActivity {
        return activity
    }

    @Provides
    @ActivityScope
    fun providePresenter(githubApiInterface: GithubApiInterface): MainContract.Presenter {
        return MainPresenter(githubApiInterface)
    }
}