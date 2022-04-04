package com.example.aplikasigithubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @field:ColumnInfo(name = "username")
    val username: String,

    @field:ColumnInfo(name = "urltoimage")
    val urlToImage: String? = null,
)