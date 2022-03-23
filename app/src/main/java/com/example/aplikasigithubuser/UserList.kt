package com.example.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserList(
    val username: String,
    val avatar: String,
): Parcelable
