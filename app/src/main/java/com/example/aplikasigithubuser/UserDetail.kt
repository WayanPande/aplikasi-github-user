package com.example.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    val username: String,
    val name: String,
    val avatar: String,
    val location: String,
    val repository: String,
    val company: String,
    val followers: String,
    val following: String,
): Parcelable
