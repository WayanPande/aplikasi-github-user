package com.example.aplikasigithubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.UserList
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import com.example.aplikasigithubuser.utils.LoadingDialog

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "USER_RESPONSE"
        const val USER_DETAIL = "USER_DETAIL"
        const val IS_USER_FAVORITE = "IS_USER_FAVORITE"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var queryText: String
    private val loadingDialog = LoadingDialog(this)
    private var isDarkMode: Boolean = false

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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
            showRecycleList(userList)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            isDarkMode = if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
        }
    }

    private fun showRecycleList(userDetailData: ArrayList<UserList>) {
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(userDetailData)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserList) {

                val factory: UserFavoriteViewModelFactory =
                    UserFavoriteViewModelFactory.getInstance(this@MainActivity)
                val favoriteViewModel: UserFavoriteViewModel by viewModels {
                    factory
                }

                favoriteViewModel.checkIfUserFavorited(data.username).observe(this@MainActivity) {
                        goToDetailPage(data, it)
                }
            }
        })
    }

    private fun goToDetailPage(data: UserList, isUserFavorite: Boolean) {
        Log.d("MAINACTIVITY",isUserFavorite.toString())
        val intentToDetailPage = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetailPage.putExtra(IS_USER_FAVORITE, isUserFavorite)
        intentToDetailPage.putExtra(USER_DETAIL, data.username)
        startActivity(intentToDetailPage)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                menu.getItem(0).icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_wb_sunny_24)
            } else {
                menu.getItem(0).icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_dark_mode_24)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        when (item.itemId) {
            R.id.toggleDisplayMode -> {
                settingViewModel.saveThemeSetting(!isDarkMode)
            }
            R.id.favoriteMenu -> {
                val intentToFavoriteActivity = Intent(this@MainActivity, UserFavoriteActivity::class.java)
                startActivity(intentToFavoriteActivity)
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        loadingDialog.dismissDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismissDialog()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingDialog.startLoadingDialog()
        } else {
            loadingDialog.dismissDialog()
        }
    }
}