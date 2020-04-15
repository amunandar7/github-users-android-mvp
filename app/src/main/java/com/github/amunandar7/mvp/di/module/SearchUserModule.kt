package com.github.amunandar7.mvp.di.module

import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.searchuser.SearchUserActivity
import com.github.amunandar7.mvp.ui.searchuser.SearchUserContract
import com.github.amunandar7.mvp.ui.searchuser.SearchUserPresenter
import dagger.Module
import dagger.Provides

@Module
class SearchUserModule(val activity: SearchUserActivity) {

    @Provides
    @ActivityScope
    fun provideActivity(): SearchUserActivity {
        return activity
    }

    @Provides
    @ActivityScope
    fun providePresenter(githubApiInterface: GithubApiInterface): SearchUserContract.Presenter {
        return SearchUserPresenter(githubApiInterface)
    }
}