package com.github.amunandar7.mvp.model

import com.google.gson.annotations.SerializedName

data class SearchResultModel(
    @SerializedName("total_count") val totalCount: Long,
    @SerializedName("incomplete_results") val incompleteResults: Long,
    val items: List<UserModel>
)