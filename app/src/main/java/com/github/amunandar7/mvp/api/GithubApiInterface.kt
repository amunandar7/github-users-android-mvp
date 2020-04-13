package com.github.amunandar7.mvp.api

import com.github.amunandar7.mvp.model.RepoModel
import com.github.amunandar7.mvp.model.SearchResultModel
import com.github.amunandar7.mvp.model.UserDetailModel
import com.github.amunandar7.mvp.model.UserModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiInterface {

    @get:GET("users")
    val users: Observable<List<UserModel>>

    @GET("users")
    fun getMoreUsers(@Query("since") lastUserId: Long): Observable<List<UserModel>>

    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Observable<SearchResultModel>

    @GET("users/{login}")
    fun getUserDetail(@Path("login") username: String): Observable<UserDetailModel>

    @GET("users/{login}/repos")
    fun getUserRepos(@Path("login") username: String): Observable<List<RepoModel>>

}