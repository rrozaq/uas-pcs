package com.rozaq.githubapp.api

import com.rozaq.githubapp.data.model.DetailUserResponse
import com.rozaq.githubapp.data.model.User
import com.rozaq.githubapp.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    val itemType: String

    companion object {
        const val token = "Authorization: token 82e4788d515c42fa29bb09789982d9fc2bf9d8d5";
    }

    @GET("search/users")
    @Headers(token)
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers(token)
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers(token)
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers(token)
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}