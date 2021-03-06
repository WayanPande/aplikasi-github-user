package com.example.aplikasigithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    var username: String,
    var name: String,
    var avatar: String,
    var location: String,
    var repository: String,
    var company: String,
    var followers: String,
    var following: String,
): Parcelable
