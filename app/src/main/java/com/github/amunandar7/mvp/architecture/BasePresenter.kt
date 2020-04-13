package com.github.amunandar7.mvp.architecture

import com.github.amunandar7.mvp.api.GithubApiInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : BaseView>(val githubApiInterface: GithubApiInterface) :
    IPresenter {
    val disposeBag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    @JvmField
    var view: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: BaseView) {
        this.view = view as T
    }

    fun addToDisposable(disposable: Disposable) {
        disposeBag.add(disposable)
    }

    override fun onViewCreated() {
        view?.initializeView()
    }

    override fun onViewStarted() {}
    override fun onViewResumed() {}
    override fun onViewStopped() {}

    override fun onViewDestroyed() {
        if (disposeBag.isDisposed) {
            disposeBag.dispose()
        }
    }
}