package com.example.aplikasigithubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.UserDetail
import com.example.aplikasigithubuser.UserList
import com.example.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.example.aplikasigithubuser.data.remote.response.ItemsItem
import com.example.aplikasigithubuser.data.remote.response.UserResponse
import com.example.aplikasigithubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {


    private val _followerList = MutableLiveData<ArrayList<UserList>>()
    val followerList: LiveData<ArrayList<UserList>> = _followerList

    private val _followingList = MutableLiveData<ArrayList<UserList>>()
    val followingList: LiveData<ArrayList<UserList>> = _followingList

    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetailDetail: LiveData<UserDetail> = _userDetail

    private val _searchedUserList = MutableLiveData<ArrayList<UserList>>()
    val searchedUserDetailList: LiveData<ArrayList<UserList>> = _searchedUserList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
        const val FOLLOWERS = "followers"
        const val FOLLOWING = "following"
        const val SEARCHED_LIST = "searched_list"
        private const val DEFAULT_SEARCH_QUERY = "dicoding"
    }

    init {
        findUser(DEFAULT_SEARCH_QUERY)
    }


    fun findUser(queryText: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(queryText)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items, SEARCHED_LIST)
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowersOrFollowingData(username: String, type: String) {

        val client = if (type == FOLLOWERS) {
            ApiConfig.getApiService().getUserFollowers(username)
        } else {
            ApiConfig.getApiService().getUserFollowing(username)
        }

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    setUserData(response.body(), type)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun findUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val userData = UserDetail(
                            responseBody.login!!,
                            responseBody.name ?: responseBody.login,
                            responseBody.avatarUrl!!,
                            responseBody.location ?: "-",
                            responseBody.publicRepos!!.toString(),
                            responseBody.company ?: "-",
                            responseBody.followers!!.toString(),
                            responseBody.following!!.toString()
                        )
                        _userDetail.value = userData
                    }
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _isLoading.value = false
            }
        })

    }


    private fun setUserData(userData: List<ItemsItem?>?, type: String) {
        val userList = ArrayList<UserList>()
        if (userData != null) {
            for (data in userData) {
                val user = UserList(
                    data?.login!!,
                    data.avatarUrl!!,
                )
                userList.add(user)
            }
        }

        when (type){
            FOLLOWERS -> _followerList.value = userList
            FOLLOWING -> _followingList.value = userList
            SEARCHED_LIST -> _searchedUserList.value = userList
        }

    }

}