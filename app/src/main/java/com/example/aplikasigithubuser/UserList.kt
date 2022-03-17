package com.example.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserList(
<<<<<<< HEAD
    val username: String,
    val avatar: String,
=======
    var username: String,
    var avatar: String,
>>>>>>> e0a4c8b... refactoring all API request using viewModel and fixing some bugs
): Parcelable
