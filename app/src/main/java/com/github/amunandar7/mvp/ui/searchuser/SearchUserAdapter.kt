package com.github.amunandar7.mvp.ui.searchuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.ui.userlist.UserListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_search_user_item.view.*
import kotlinx.android.synthetic.main.layout_search_user_loading.view.*

class SearchUserAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val users = mutableListOf<UserModel>()

    private var listener: SearchUserInteractionListener? = null

    var isLoading = false

    override fun getItemViewType(position: Int): Int {
        return if (position >= users.size) UserListAdapter.VIEW_TYPE_LOADING else UserListAdapter.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == UserListAdapter.VIEW_TYPE_ITEM)
            UserItemHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.layout_search_user_item, parent, false)
            )
        else LoadingViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_search_user_loading, parent, false)
        )
    }

    override fun getItemCount(): Int {
        var count = users.size
        if (isLoading) {
            if (count == 0) // when first load or swipe refresh
                count += 8
            else
                count += 2
        }
        return count
    }

    fun getRealCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserItemHolder) {
            val userModel = users.get(position)
            holder.bind(userModel)
            holder.itemView.setOnClickListener {
                listener?.onSearchItemClicked(userModel)
            }
        } else if (holder is LoadingViewHolder)
            holder.bind()
    }

    fun setListener(listener: SearchUserInteractionListener) {
        this.listener = listener
    }

    fun addUsers(users: List<UserModel>) {
        this.users.addAll(users)
        isLoading = false
        notifyDataSetChanged()
    }

    fun onLoadMore() {
        isLoading = true
        notifyDataSetChanged()
    }

    fun clearData() {
        users.clear()
        notifyDataSetChanged()
    }

    class UserItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(userModel: UserModel) {
            itemView.searchUserItemName.text = userModel.login
            Picasso.with(itemView.context).load(userModel.avatarUrl)
                .into(itemView.searchUserItemAvatar)
        }
    }

    class LoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            itemView.searchUserItemShimmer.startShimmer()
        }
    }

    interface SearchUserInteractionListener {
        fun onSearchItemClicked(userModel: UserModel)
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}