package com.dicoding.githubusersearch.data.remote.retrofit

import com.dicoding.githubusersearch.data.remote.response.DetailUserResponse
import com.dicoding.githubusersearch.data.remote.response.GithubResponse
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") keyword: String
    ): Call<GithubResponse>
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}