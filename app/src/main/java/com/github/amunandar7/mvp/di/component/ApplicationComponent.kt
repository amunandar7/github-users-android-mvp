package com.github.amunandar7.mvp.di.component

import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.di.module.ApplicationModule
import com.github.amunandar7.mvp.di.module.GithubApiModule
import com.github.amunandar7.mvp.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class, GithubApiModule::class))
interface ApplicationComponent {

    var githubApiInterface: GithubApiInterface

    fun inject(application: BaseApp)
}