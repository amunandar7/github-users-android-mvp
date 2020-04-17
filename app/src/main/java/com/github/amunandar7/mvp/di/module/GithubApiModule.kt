package com.github.amunandar7.mvp.di.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.BuildConfig
import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.api.InterceptorManager
import com.github.amunandar7.mvp.api.RxErrorHandlingCallAdapterFactory
import com.github.amunandar7.mvp.di.scope.ApplicationScope
import com.github.amunandar7.mvp.util.Constant
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


@Module
class GithubApiModule(val baseApp: BaseApp) {

    @Provides
    @ApplicationScope
    fun provideGithubApiInterface(retrofit: Retrofit): GithubApiInterface {
        return retrofit.create(GithubApiInterface::class.java)
    }

    @Provides
    @ApplicationScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_API_URL)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideOkHttpCleint(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpCacheDirectory = File(baseApp.cacheDir, "responses")
        val cacheSize = 20 * 1024 * 1024 // 20 MiB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(InterceptorManager.rewriteResponseInterceptor)
            .addInterceptor(InterceptorManager.rewriteResponseInterceptor)
            .readTimeout(Constant.TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .connectTimeout(Constant.TIMEOUT_IN_SECOND, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addInterceptor(ChuckInterceptor(baseApp))
            client.addInterceptor(httpLoggingInterceptor)
            client.networkInterceptors().add(StethoInterceptor())
            client.hostnameVerifier { _, _ -> true }
        }

        return client.build()
    }

    @Provides
    @ApplicationScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}