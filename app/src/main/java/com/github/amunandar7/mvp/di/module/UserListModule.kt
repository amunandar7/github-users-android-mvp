package com.github.amunandar7.mvp.di.module

import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.userlist.UserListActivity
import com.github.amunandar7.mvp.ui.userlist.UserListContract
import com.github.amunandar7.mvp.ui.userlist.UserListPresenter
import dagger.Module
import dagger.Provides

@Module
class UserListModule(
    var activity: UserListActivity
) {

    @Provides
    @ActivityScope
    fun provideActivity(): UserListActivity {
        return activity
    }

    @Provides
    @ActivityScope
    fun providePresenter(githubApiInterface: GithubApiInterface): UserListContract.Presenter {
        return UserListPresenter(githubApiInterface)
    }
}