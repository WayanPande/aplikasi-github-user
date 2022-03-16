package com.example.aplikasigithubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel: ViewModel(){

    private val _followerList = MutableLiveData<ArrayList<User>>()
    val followerList : LiveData<ArrayList<User>> = _followerList

    private val _followingList = MutableLiveData<ArrayList<User>>()
    val followingList : LiveData<ArrayList<User>> = _followingList

    companion object{
        private const val TAG = "MainViewModel"
        const val FOLLOWERS = "followers"
        const val FOLLOWING = "following"
    }

    fun getFollowersOrFollowingData(username: String, type: String){

        val client = if (type == FOLLOWERS){
            ApiConfig.getApiService().getUserFollowers(username)
        }else{
            ApiConfig.getApiService().getUserFollowing(username)
        }

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    setUserData(response.body(), type)
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    private fun setUserData(userData: List<ItemsItem?>?, type: String){
        val userList = ArrayList<User>()
        if (userData != null) {
            for (data in userData){
                val user = User(
                    data?.login!!,
                    data.login,
                    data.avatarUrl!!,
                    "test",
                    "test",
                    "test",
                    "test",
                    "test"
                )
                userList.add(user)
            }
        }

        if (type == FOLLOWERS)  _followerList.value = userList else _followingList.value = userList

    }

}