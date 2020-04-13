package com.github.amunandar7.mvp.ui.main

import com.github.amunandar7.mvp.architecture.BaseView
import com.github.amunandar7.mvp.architecture.IPresenter
import com.github.amunandar7.mvp.model.UserModel

class MainContract {
    interface View : BaseView {
        fun onLoadUserDataSuccess(users: List<UserModel>)
        fun onLoadUserDataFailed(throwable: Throwable)
    }

    interface Presenter : IPresenter {
        fun loadMoreUserData(lastUserId: Long)
        fun searchUserData(q: String)
    }
}