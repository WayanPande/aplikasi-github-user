package com.example.aplikasigithubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") q: String
    ): Call<UserResponse>
}