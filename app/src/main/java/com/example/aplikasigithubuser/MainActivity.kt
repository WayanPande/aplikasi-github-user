package com.example.aplikasigithubuser

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "USER_RESPONSE"
        const val USER_DETAIL = "USER_DETAIL"
    }

    private val list = ArrayList<User>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var queryText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = binding.svSearch

        queryText = "dicoding"
        findUser()

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                if (query != null) {
                    queryText = query
                }
                findUser()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showRecycleList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetailPage = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetailPage.putExtra(USER_DETAIL, data.username)
                startActivity(intentToDetailPage)
            }
        })
    }

    private fun findUser(){
        showLoading(true)
        val client = ApiConfig.getApiService().getUser(queryText)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        Log.d("MAIN_ACTIVITY_OK",
                            responseBody.toString()
                        )
                        setUserData(responseBody.items)
                    }
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(userData: List<ItemsItem?>?){
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
        list.clear()
        list.addAll(userList)
        showRecycleList()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}