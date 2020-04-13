package com.github.amunandar7.mvp.model

import com.google.gson.annotations.SerializedName

data class UserDetailModel(
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)