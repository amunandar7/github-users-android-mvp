package com.github.amunandar7.mvp.ui.userlist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.architecture.BaseActivity
import com.github.amunandar7.mvp.di.component.ApplicationComponent
import com.github.amunandar7.mvp.di.component.DaggerUserListComponent
import com.github.amunandar7.mvp.di.module.UserListModule
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.util.notNull
import com.github.amunandar7.mvp.view.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_user_list.*


class UserListActivity : BaseActivity<UserListContract.Presenter>(), UserListContract.View,
    UserListAdapter.UserListInteractionListener {

    private val userListAdapter: UserListAdapter by lazy { UserListAdapter(this) }
    lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
    }

    override fun onPrepare() {
        super.onPrepare()
        val applicationComponent: ApplicationComponent =
            BaseApp.instance.getApplicationComponent()
        val activityComponent = DaggerUserListComponent.builder()
            .applicationComponent(applicationComponent)
            .userListModule(UserListModule(this)).build()
        activityComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        // Associate searchable configuration with the SearchView
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return query != null
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("17412", "newText = $newText")
                return newText != null
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun initializeView() {
        userListLayout.isRefreshing = true
        initializeListAdapter()
        initializeListener()
    }

    private fun initializeListAdapter() {
        val layoutManager = GridLayoutManager(this, 2)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun getFooterViewType(defaultNoFooterViewType: Int): Int {
                return defaultNoFooterViewType
            }

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                userListAdapter.getLastId().notNull {
                    userListAdapter.onLoadMore()
                    presenter.loadMoreUserData(it)
                }
            }
        }
        userListAdapter.setListener(this)
        userlist.addOnScrollListener(scrollListener)
        userlist.layoutManager = layoutManager
        userlist.adapter = userListAdapter
    }

    private fun initializeListener() {
        userListLayout.setOnRefreshListener {
            userListAdapter.isLoading = true
            userListAdapter.clearData()
            scrollListener.reset()
            presenter.refreshData()
        }
    }

    override fun onUserItemCLicked(userModel: UserModel) {
        Toast.makeText(this, "Id ${userModel.id} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSwipeRefresh() {
        userListLayout.isRefreshing = true
        userlist.visibility = View.VISIBLE
        noResult.visibility = View.GONE
    }

    override fun onLoadUserDataSuccess(users: List<UserModel>) {
        userListAdapter.addUsers(users)
        if (userListAdapter.itemCount > 0) {
            userlist.visibility = View.VISIBLE
            noResult.visibility = View.GONE
        } else {
            userlist.visibility = View.GONE
            noResult.visibility = View.VISIBLE
        }
        userListLayout.isRefreshing = false

    }

    override fun onLoadUserDataFailed(throwable: Throwable) {
        throwable.printStackTrace()
    }

}
