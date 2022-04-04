package com.example.aplikasigithubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.UserList
import com.example.aplikasigithubuser.databinding.ActivityUserFavoriteBinding

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding
    private var isDarkMode: Boolean = false

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite User"

        val factory: UserFavoriteViewModelFactory = UserFavoriteViewModelFactory.getInstance(this)
        val favoriteViewModel: UserFavoriteViewModel by viewModels {
            factory
        }

        favoriteViewModel.getFavoriteUser().observe(this) { favoriteUser ->
            val userList = ArrayList<UserList>()

            for (i in favoriteUser.indices) {
                val user = UserList(
                    favoriteUser[i].username,
                    favoriteUser[i].urlToImage!!
                )
                userList.add(user)
            }

            showRecycleList(userList)
        }



        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun showRecycleList(userDetailData: ArrayList<UserList>) {
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserFavoriteAdapter(userDetailData)
        binding.rvFavorite.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserList) {
                val intentToDetailPage = Intent(this@UserFavoriteActivity, DetailActivity::class.java)
                intentToDetailPage.putExtra(MainActivity.USER_DETAIL, data.username)
                intentToDetailPage.putExtra(MainActivity.IS_USER_FAVORITE, true)
                startActivity(intentToDetailPage)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)


        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.favoriteMenu)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        when (item.itemId) {
            R.id.toggleDisplayMode -> {
                settingViewModel.saveThemeSetting(!isDarkMode)
            }
        }
        return true
    }

}