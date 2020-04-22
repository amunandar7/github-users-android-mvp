package com.github.amunandar7.mvp.ui.searchuser

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.architecture.BaseActivity
import com.github.amunandar7.mvp.di.component.ApplicationComponent
import com.github.amunandar7.mvp.di.component.DaggerSearchUserComponent
import com.github.amunandar7.mvp.di.module.SearchUserModule
import com.github.amunandar7.mvp.eventbus.ApiErrorEvent
import com.github.amunandar7.mvp.model.SearchResultModel
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.util.HttpErrorCode
import com.github.amunandar7.mvp.view.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_search_user.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.net.ssl.HttpsURLConnection

class SearchUserActivity : BaseActivity<SearchUserContract.Presenter>(), SearchUserContract.View,
    SearchUserAdapter.SearchUserInteractionListener {

    val searchUserAdapter: SearchUserAdapter by lazy { SearchUserAdapter(this) }

    var searchValue: String = ""

    lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
    }

    override fun onPrepare() {
        super.onPrepare()
        val applicationComponent: ApplicationComponent =
            BaseApp.instance.getApplicationComponent()
        val activityComponent = DaggerSearchUserComponent.builder()
            .applicationComponent(applicationComponent)
            .searchUserModule(SearchUserModule(this))
            .build()
        activityComponent.inject(this)
    }

    override fun initializeView() {
        setSearchResultAdapter()
        ic_back_button.setOnClickListener {
            finish()
        }
        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onSearching(s.toString())
            }
        })
        searchClearButton.setOnClickListener {
            searchValue = ""
            searchField.setText("")
        }
    }

    private fun onSearching(newString: String) {
        searchValue = newString
        searchUserAdapter.clearData()
        if (newString.length > 0) {
            searchUserAdapter.isLoading = true
            Handler().postDelayed({
                if (newString.equals(searchValue)) {
                    scrollListener.reset()
                    presenter.searchUser(searchValue, 1)
                }
            }, SEARCH_DELAY)
        } else searchUserAdapter.isLoading = false
        showSearchResult()

    }

    private fun setSearchResultAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun getFooterViewType(defaultNoFooterViewType: Int): Int {
                return defaultNoFooterViewType
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                searchUserAdapter.onLoadMore()
                presenter.searchUser(searchValue, page)
            }
        }
        searchUserAdapter.setListener(this)
        searchResult.addOnScrollListener(scrollListener)
        searchResult.layoutManager = layoutManager
        searchResult.adapter = searchUserAdapter
    }

    override fun onSearchItemClicked(userModel: UserModel) {
        //TODO go to user detail
    }

    override fun onSearchUserSuccess(searchResultModel: SearchResultModel) {
        searchUserAdapter.addUsers(searchResultModel.items)
        if (searchUserAdapter.itemCount > 0) showSearchResult() else onNoResult()
    }

    override fun onSearchUserFailed(throwable: Throwable) {
        throwable.printStackTrace()
        searchUserAdapter.isLoading = false
        searchUserAdapter.notifyDataSetChanged()
    }

    private fun showSearchResult() {
        searchResult.visibility = View.VISIBLE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onNoResult() {
        searchResult.visibility = View.GONE
        noResultLayout.visibility = View.VISIBLE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onNoNetworkAvailable() {
        searchResult.visibility = View.GONE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.VISIBLE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onReachApiLimit() {
        searchResult.visibility = View.GONE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onApiErrorEvent(event: ApiErrorEvent) {
        if (searchUserAdapter.itemCount == 0) {
            when (event.code) {
                HttpsURLConnection.HTTP_FORBIDDEN -> onReachApiLimit()
                HttpErrorCode.NO_NETWORK_AVAILABLE -> onNoNetworkAvailable()
            }
        }
    }

    companion object {
        const val SEARCH_DELAY: Long = 500
    }

}
