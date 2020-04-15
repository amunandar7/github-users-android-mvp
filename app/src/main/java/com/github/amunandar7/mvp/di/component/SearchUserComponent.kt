package com.github.amunandar7.mvp.di.component

import com.github.amunandar7.mvp.di.module.SearchUserModule
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.searchuser.SearchUserActivity
import dagger.Component

@ActivityScope
@Component(
    modules = arrayOf(SearchUserModule::class),
    dependencies = arrayOf(ApplicationComponent::class)
)
interface SearchUserComponent {
    fun inject(activity: SearchUserActivity)
}