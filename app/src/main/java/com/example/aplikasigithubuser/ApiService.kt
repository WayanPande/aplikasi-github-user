package com.example.aplikasigithubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(
        @Query("q") q: String
    ): Call<UserResponse>

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