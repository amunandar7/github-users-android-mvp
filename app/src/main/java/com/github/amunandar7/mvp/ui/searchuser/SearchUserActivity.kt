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
import com.github.amunandar7.mvp.model.SearchResultModel
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.view.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_search_user.*

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
                val newString = s.toString()
                searchValue = newString
                Handler().postDelayed({
                    if (newString.equals(searchValue)) {
                        if (newString.length > 0) {
                            searchUserAdapter.isLoading = true
                            searchUserAdapter.clearData()
                            scrollListener.reset()
                            searchUserAdapter.clearData()
                            presenter.searchUser(searchValue, 1)
                        } else {
                            searchUserAdapter.isLoading = false
                            searchUserAdapter.clearData()
                        }
                    }
                }, SEARCH_DELAY)
            }
        })
        searchClearButton.setOnClickListener {
            searchValue = ""
            searchField.setText("")
        }
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
        if (searchUserAdapter.itemCount > 0) {
            searchResult.visibility = View.VISIBLE
            noResult.visibility = View.GONE
        } else {
            searchResult.visibility = View.GONE
            noResult.visibility = View.VISIBLE
        }
    }

    override fun onSearchUserFailed(throwable: Throwable) {
        throwable.printStackTrace()
        searchUserAdapter.isLoading = false
        searchUserAdapter.notifyDataSetChanged()
    }

    companion object {
        const val SEARCH_DELAY: Long = 500
    }

}
