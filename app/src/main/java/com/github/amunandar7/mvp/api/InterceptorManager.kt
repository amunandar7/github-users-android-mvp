package com.github.amunandar7.mvp.api

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor

object InterceptorManager {

    var context: Application? = null


    val rewriteResponseInterceptor = Interceptor { chain ->
        val request = chain.request()
        if (!okhttp3.internal.http.HttpMethod.invalidatesCache(request.method()) && request.header(
                CacheFor.KEY
            ) != null
        ) {
            if (request.header(CacheStrategy.KEY) != null || !isNetworkAvailable()) {
                try {
                    val maxAge = request.header(CacheFor.KEY)?.toInt() ?: 0
                    val cacheControl = okhttp3.CacheControl.Builder()
                        .maxAge(maxAge, java.util.concurrent.TimeUnit.SECONDS)
                        .build()
                    val cacheRequest = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build()
                    val response = chain.proceed(cacheRequest)
                    return@Interceptor response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", cacheControl.toString())
                        .build()
                } catch (ex: java.lang.IllegalStateException) {

                }
            }
        }
        return@Interceptor chain.proceed(chain.request())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}