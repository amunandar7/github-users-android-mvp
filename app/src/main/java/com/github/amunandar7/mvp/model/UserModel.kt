package com.github.amunandar7.mvp.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String
)