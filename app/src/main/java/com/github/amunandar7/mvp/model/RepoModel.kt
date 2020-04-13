package com.github.amunandar7.mvp.model

import com.google.gson.annotations.SerializedName

data class RepoModel(
    val name: String,
    @SerializedName("stargazers_count") val stargazersCount: Long,
    @SerializedName("watchers_count") val watchersCount: Long,
    @SerializedName("forks_count") val forksCount: Long,
    @SerializedName("open_issues_count") val openIssuesCount: Long
)