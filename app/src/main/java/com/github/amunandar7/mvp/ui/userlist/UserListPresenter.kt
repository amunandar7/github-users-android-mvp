package com.github.amunandar7.mvp.ui.userlist

import com.github.amunandar7.mvp.api.GithubApiInterface
import com.github.amunandar7.mvp.architecture.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserListPresenter(githubApiInterface: GithubApiInterface) :
    UserListContract.Presenter, BasePresenter<UserListContract.View>(githubApiInterface) {

    override fun loadMoreUserData(lastUserId: Long) {

    }

    override fun searchUserData(q: String) {
    }

    override fun onViewCreated() {
        view?.initializeView()
        githubApiInterface.users.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                view?.onLoadUserDataSuccess(it)
            }, {
                view?.onLoadUserDataFailed(it)
            })
    }

}