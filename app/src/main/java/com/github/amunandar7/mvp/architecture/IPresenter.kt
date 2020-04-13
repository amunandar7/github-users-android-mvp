package com.github.amunandar7.mvp.architecture

interface IPresenter {
    fun attachView(view: BaseView)

    fun onViewCreated()
    fun onViewStarted()
    fun onViewResumed()
    fun onViewStopped()

    fun onViewDestroyed()
}