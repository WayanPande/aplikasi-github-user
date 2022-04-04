package com.example.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aplikasigithubuser.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query("DELETE FROM user WHERE username = :username")
     fun removeUserFromFavorite(username: String)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username)")
    fun isUserFavorited(username: String): LiveData<Boolean>

    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(news: List<UserEntity>)
}