package com.github.amunandar7.mvp.api

import com.github.amunandar7.mvp.model.RepoModel
import com.github.amunandar7.mvp.model.SearchResultModel
import com.github.amunandar7.mvp.model.UserDetailModel
import com.github.amunandar7.mvp.model.UserModel
import com.github.amunandar7.mvp.util.Constant
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiInterface {

    @get:GET("users?per_page=${Constant.TOTAL_PER_PAGE}")
    val users: Observable<List<UserModel>>

    @GET("users?per_page=${Constant.TOTAL_PER_PAGE}")
    fun getMoreUsers(@Query("since") lastUserId: Long): Observable<List<UserModel>>

    @GET("search/users?per_page=${Constant.TOTAL_PER_PAGE}")
    fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Observable<SearchResultModel>

    @GET("users/{login}")
    fun getUserDetail(@Path("login") username: String): Observable<UserDetailModel>

    @GET("users/{login}/repos")
    fun getUserRepos(@Path("login") username: String): Observable<List<RepoModel>>

}