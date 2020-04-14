package com.github.amunandar7.mvp.model

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class UserDetailModel(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url") val avatarUrl: String,
    val name: String,
    val company: String,
    val blog: String,
    val location: String,
    val email: String,
    val hireable: String,
    val bio: String,
    @SerializedName("public_repos") val publicRepos: String,
    @SerializedName("public_gists") val publicGists: String,
    val followers: Long,
    val following: Long,
    @SerializedName("created_at") val createdAt: DateTime
)