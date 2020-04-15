package com.github.amunandar7.mvp.ui.searchuser

import com.github.amunandar7.mvp.architecture.BaseView
import com.github.amunandar7.mvp.architecture.IPresenter
import com.github.amunandar7.mvp.model.SearchResultModel

class SearchUserContract {

    interface View : BaseView {
        fun onSearchUserSuccess(searchResultModel: SearchResultModel)
        fun onSearchUserFailed(throwable: Throwable)
    }

    interface Presenter : IPresenter {
        fun searchUser(q: String, page: Int)
    }
}