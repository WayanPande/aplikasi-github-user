package com.example.aplikasigithubuser

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "USER_RESPONSE"
        const val USER_DETAIL = "USER_DETAIL"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var queryText: String
    private val loadingDialog = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = binding.svSearch

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    queryText = query
                }
                searchView.clearFocus()
                mainViewModel.findUser(queryText)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mainViewModel.searchedUserDetailList.observe(this) { userList ->
            if (userList.size == 0) {
                Toast.makeText(this, "User Tidak Ditemukan!", Toast.LENGTH_LONG).show()
            }
            showRecycleList(userList)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showRecycleList(userDetailData: ArrayList<UserList>) {
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(userDetailData)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserList) {
                val intentToDetailPage = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetailPage.putExtra(USER_DETAIL, data.username)
                startActivity(intentToDetailPage)
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingDialog.startLoadingDialog()
        } else {
            loadingDialog.dismissDialog()
        }
    }
}