package com.example.aplikasigithubuser.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasigithubuser.data.UserRepository
import com.example.aplikasigithubuser.data.di.Injection

class UserFavoriteViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            return UserFavoriteViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserFavoriteViewModelFactory? = null
        fun getInstance(context: Context): UserFavoriteViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UserFavoriteViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}