package com.example.aplikasigithubuser.data.remote.retrofit

import com.example.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.example.aplikasigithubuser.data.remote.response.ItemsItem
import com.example.aplikasigithubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("search/users")
    @Headers("Authorization: token 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")
    suspend fun getUserFavourite(
        @Query("q") q: String
    ): UserResponse

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ):Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}