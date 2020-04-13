package com.github.amunandar7.mvp.di.module

import android.app.Application
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.di.scope.ApplicationContext
import com.github.amunandar7.mvp.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideApplication(): Application {
        return baseApp
    }
}