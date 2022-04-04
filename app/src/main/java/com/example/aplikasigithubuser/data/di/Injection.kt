package com.example.aplikasigithubuser.data.di

import android.content.Context
import com.example.aplikasigithubuser.data.UserRepository
import com.example.aplikasigithubuser.data.local.room.UserDatabase
import com.example.aplikasigithubuser.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}