package com.github.amunandar7.mvp.ui.searchuser

import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.architecture.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchUserPresenter(githubApiInterface: GithubApiInterface) :
    BasePresenter<SearchUserContract.View>(githubApiInterface), SearchUserContract.Presenter {
    override fun searchUser(q: String, page: Int) {
        githubApiInterface.searchUsers(q, page).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                view?.onSearchUserSuccess(it)
            }, {
                view?.onSearchUserFailed(it)
            })
    }
}