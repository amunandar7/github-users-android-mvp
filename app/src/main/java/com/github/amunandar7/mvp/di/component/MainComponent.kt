package com.github.amunandar7.mvp.di.component

import com.github.amunandar7.mvp.di.module.MainModule
import com.github.amunandar7.mvp.di.scope.ActivityScope
import com.github.amunandar7.mvp.ui.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
    modules = arrayOf(MainModule::class),
    dependencies = arrayOf(ApplicationComponent::class)
)
interface MainComponent {
    fun inject(activity: MainActivity)
}