package com.github.amunandar7.mvp.ui.userlist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
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
import kotlinx.android.synthetic.main.activity_user_list.*


class UserListActivity : BaseActivity<UserListContract.Presenter>(), UserListContract.View,
    UserListAdapter.UserListInteractionListener {

    private val userListAdapter: UserListAdapter by lazy { UserListAdapter(this) }

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
        val layoutManager = GridLayoutManager(this, 2)
        userlist.layoutManager = layoutManager
        userListAdapter.setListener(this)
        userlist.adapter = userListAdapter
    }

    override fun onUserItemCLicked(userModel: UserModel) {
        //TODO go to user detail page
        Toast.makeText(this, "Id ${userModel.id} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLoadUserDataSuccess(users: List<UserModel>) {
        userListAdapter.addUsers(users)
    }

    override fun onLoadUserDataFailed(throwable: Throwable) {
        throwable.printStackTrace()
    }

}
