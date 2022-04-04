package com.example.aplikasigithubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.aplikasigithubuser.data.local.entity.UserEntity
import com.example.aplikasigithubuser.data.local.room.UserDao
import com.example.aplikasigithubuser.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao) {

    fun insertUser(userName: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserFavourite(userName)
            val users = response.items?.filter {
                it?.login.equals(userName)
            }
            val userList = users?.map { user ->
                UserEntity(
                    0,
                    user?.login ?: "",
                    user?.avatarUrl,
                )
            }
            if (userList != null) {
                userDao.insertUser(userList)
            }
        }catch (e: Exception) {
            Log.d("UserRepository", "getUserFavourite: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getFavoriteUser().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    fun checkUser(username:String): LiveData<Boolean> {
        return userDao.isUserFavorited(username)
    }

    fun removeUser(username: String) {
        userDao.removeUserFromFavorite(username)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also { instance = it }
    }
}