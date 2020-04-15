package com.github.amunandar7.mvp.ui.userlist

import com.github.amunandar7.mvp.architecture.BaseView
import com.github.amunandar7.mvp.architecture.IPresenter
import com.github.amunandar7.mvp.model.UserModel

class UserListContract {
    interface View : BaseView {
        fun onSwipeRefresh()
        fun onLoadUserDataSuccess(users: List<UserModel>)
        fun onLoadUserDataFailed(throwable: Throwable)
    }

    interface Presenter : IPresenter {
        fun refreshData()
        fun loadMoreUserData(lastUserId: Long)
    }
}