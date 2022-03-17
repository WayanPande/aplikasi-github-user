package com.example.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserList(
    var username: String,
    var avatar: String,
): Parcelable
