package com.example.aplikasigithubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasigithubuser.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun insertUserToRoom(username: String) = userRepository.insertUser(username)


    fun getFavoriteUser() = userRepository.getFavoriteUser()

    fun checkIfUserFavorited(username: String) = userRepository.checkUser(username)

    fun deleteUserFavorite(username: String) {
        viewModelScope.launch (Dispatchers.IO) {
            userRepository.removeUser(username)
        }
    }
}