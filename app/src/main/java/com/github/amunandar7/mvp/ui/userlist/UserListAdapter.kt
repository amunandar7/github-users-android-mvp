package com.github.amunandar7.mvp.ui.userlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.amunandar7.mvp.R
import com.github.amunandar7.mvp.model.UserModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_user_list_item.view.*

class UserListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val users = mutableListOf<UserModel>()

    private var listener: UserListInteractionListener? = null

    var isLoading = true

    override fun getItemViewType(position: Int): Int {
        return if (position >= users.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM)
            UserItemHolder(
                LayoutInflater.from(context).inflate(R.layout.layout_user_list_item, parent, false)
            )
        else LoadingViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_user_list_item, parent, false)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserItemHolder) {
            val userModel = users.get(position)
            holder.bind(userModel)
            holder.itemView.setOnClickListener {
                listener?.onUserItemCLicked(userModel)
            }
        } else if (holder is LoadingViewHolder)
            holder.bind()
    }

    fun getLastId(): Long? {
        if (itemCount > 0)
            return users.get(itemCount - 1).id
        return null
    }

    fun setListener(listener: UserListInteractionListener) {
        this.listener = listener
    }

    fun addUsers(users: List<UserModel>) {
        this.users.addAll(users)
        isLoading = false
        notifyDataSetChanged()
    }


    fun clearData() {
        users.clear()
        notifyDataSetChanged()
    }

    class UserItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(userModel: UserModel) {
            itemView.userLogin.text = userModel.login
            Picasso.with(itemView.context).load(userModel.avatarUrl).into(itemView.userAvatar)
        }
    }

    class LoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
//            itemView.userItemShimmer.startShimmer()
        }
    }

    interface UserListInteractionListener {
        fun onUserItemCLicked(userModel: UserModel)
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_LOADING = 1
    }
}