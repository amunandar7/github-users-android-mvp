package com.github.amunandar7.mvp.architecture

interface BaseView {
    /**
     * called before @setContentView
     * don't access view inside this method
     */
    fun onPrepare() {}

    fun initializeView()
}