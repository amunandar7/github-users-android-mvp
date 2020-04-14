package com.github.amunandar7.mvp.di.component

import com.github.amunandar7.mvp.di.module.UserListModule
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.userlist.UserListActivity
import dagger.Component

@ActivityScope
@Component(
    modules = arrayOf(UserListModule::class),
    dependencies = arrayOf(ApplicationComponent::class)
)
interface UserListComponent {
    fun inject(activity: UserListActivity)
}