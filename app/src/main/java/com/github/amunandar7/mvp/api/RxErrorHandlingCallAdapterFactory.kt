package com.github.amunandar7.mvp.api

import com.github.amunandar7.mvp.eventbus.ApiErrorEvent
import com.github.amunandar7.mvp.util.HttpErrorCode
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import java.net.UnknownHostException

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {
    private val original = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(
            original.get(
                returnType,
                annotations,
                retrofit
            ) as CallAdapter<*, *>
        )
    }

    private class RxCallAdapterWrapper<R, T>(private val wrapped: CallAdapter<R, T>) :
        CallAdapter<R, T> {

        private val eventBus = EventBus.getDefault()

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        @Suppress("UNCHECKED_CAST")
        override fun adapt(call: Call<R>): T {
            return if (wrapped.adapt(call) is Completable) {
                (wrapped.adapt(call) as Completable).observeOn(AndroidSchedulers.mainThread())
                    .doOnError { catchThrowableError(it) } as T
            } else {
                (wrapped.adapt(call) as Observable<*>).observeOn(AndroidSchedulers.mainThread())
                    .doOnError { catchThrowableError(it) } as T
            }
        }

        private fun catchThrowableError(throwable: Throwable) {
            if (throwable is UnknownHostException) {
                val event = ApiErrorEvent(HttpErrorCode.NO_NETWORK_AVAILABLE, throwable.message)
                eventBus.post(event)
            } else if (throwable is HttpException) {
                val event = ApiErrorEvent(throwable.code(), throwable.message())
                eventBus.post(event)
            }
        }
    }

    companion object {

        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }


}