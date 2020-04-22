package com.github.amunandar7.mvp.ui.userlist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.github.amunandar7.mvp.BaseApp
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.architecture.BaseActivity
import com.github.amunandar7.mvp.di.component.ApplicationComponent
import com.github.amunandar7.mvp.di.component.DaggerUserListComponent
import com.github.amunandar7.mvp.di.module.UserListModule
import com.github.amunandar7.mvp.eventbus.ApiErrorEvent
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.ui.searchuser.SearchUserActivity
import com.github.amunandar7.mvp.util.HttpErrorCode
import com.github.amunandar7.mvp.util.notNull
import com.github.amunandar7.mvp.view.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.net.ssl.HttpsURLConnection


class UserListActivity : BaseActivity<UserListContract.Presenter>(), UserListContract.View,
    UserListAdapter.UserListInteractionListener {
    private var doubleBackToExit = false
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

    override fun initializeView() {
        initializeToolbar()
        userListLayout.isRefreshing = true
        initializeListAdapter()
        initializeListener()
    }

    private fun initializeToolbar() {
        val toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.activity_user_list_title)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                openSearchPage()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun openSearchPage() {
        startActivity(Intent(this, SearchUserActivity::class.java))
    }

    override fun onSwipeRefresh() {
        userListLayout.isRefreshing = true
        userlist.visibility = View.VISIBLE
        noResultLayout.visibility = View.GONE
    }

    override fun onLoadUserDataSuccess(users: List<UserModel>) {
        userListAdapter.addUsers(users)
        if (userListAdapter.itemCount > 0) showUserList() else onUsersEmpty()
        userListLayout.isRefreshing = false

    }

    override fun onLoadUserDataFailed(throwable: Throwable) {
        userListLayout.isRefreshing = false
        userListAdapter.isLoading = false
        userListAdapter.notifyDataSetChanged()
        throwable.printStackTrace()
    }

    private fun showUserList() {
        userlist.visibility = View.VISIBLE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onUsersEmpty() {
        userlist.visibility = View.GONE
        noResultLayout.visibility = View.VISIBLE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onNoNetworkAvailable() {
        userlist.visibility = View.GONE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.VISIBLE
        reachLimitLayout.visibility = View.GONE
    }

    private fun onReachApiLimit() {
        userlist.visibility = View.GONE
        noResultLayout.visibility = View.GONE
        noInternetLayout.visibility = View.GONE
        reachLimitLayout.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExit = true
        Toast.makeText(this, R.string.double_back_to_exit, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExit = false }, 3000)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onApiErrorEvent(event: ApiErrorEvent) {
        if (userListAdapter.itemCount == 0) {
            when (event.code) {
                HttpsURLConnection.HTTP_FORBIDDEN -> onReachApiLimit()
                HttpErrorCode.NO_NETWORK_AVAILABLE -> onNoNetworkAvailable()
            }
        }
    }

}
