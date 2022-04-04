package com.example.aplikasigithubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.UserDetail
import com.example.aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.aplikasigithubuser.utils.IsUserConnectedToInternet
import com.example.aplikasigithubuser.utils.LoadingDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private val loadingDialog = LoadingDialog(this)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var isDarkMode: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(MainActivity.USER_DETAIL)
        var isUserFavorite = intent.getBooleanExtra(MainActivity.IS_USER_FAVORITE, false)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "User Detail"

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
        if (username != null) {
            mainViewModel.apply {
                findUserDetail(username)
                getFollowersOrFollowingData(username, MainViewModel.FOLLOWERS)
                getFollowersOrFollowingData(username, MainViewModel.FOLLOWING)
            }
        }

        val factory: UserFavoriteViewModelFactory = UserFavoriteViewModelFactory.getInstance(this)
        val favoriteViewModel: UserFavoriteViewModel by viewModels {
            factory
        }


        favoriteViewModel.checkIfUserFavorited(username!!).observe(this) {
            isUserFavorite = it
            isIconChecked(it)
        }

        mainViewModel.userDetailDetail.observe(this) { userDetailData ->
            setUiText(userDetailData)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        if (!IsUserConnectedToInternet().check(this)) {

            favoriteViewModel.getFavoriteUser().observe(this) { favoriteUser ->

                for (i in favoriteUser.indices) {
                    if (favoriteUser[i].username == username) {
                        val userList = UserDetail(
                            favoriteUser[i].username,
                            favoriteUser[i].username,
                            favoriteUser[i].urlToImage!!,
                            "","","","",""
                        )
                        setUiText(userList)
                        break
                    }
                }
            }
        }


        binding.fabAdd.setOnClickListener {

            if (isUserFavorite) {
                favoriteViewModel.deleteUserFavorite(binding.tvDetailUsername.text.toString())
                isIconChecked(false)
                Toast.makeText(this, "User removed from favorite", Toast.LENGTH_SHORT).show()
            } else {
                favoriteViewModel.insertUserToRoom(binding.tvDetailUsername.text.toString())
                    .observe(this) {}
                isIconChecked(true)
                Toast.makeText(this, "User added to favorite", Toast.LENGTH_SHORT).show()
            }
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

    private fun isIconChecked(isUserFavorite: Boolean) {
        if (isUserFavorite) {
            binding.fabAdd.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fabAdd.context,
                    R.drawable.ic_baseline_favorite_24
                )
            )
        } else {
            binding.fabAdd.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.fabAdd.context,
                    R.drawable.ic_baseline_favorite_border_24
                )
            )
        }
    }

    private fun setUiText(data: UserDetail) {

        binding.apply {
            tvDetailName.text = data.name
            tvDetailUsername.text = data.username
            tvFollowers.text = data.followers
            tvFollowing.text = data.following
            tvCompany.text = data.company
            tvLocation.text = data.location
            tvRepository.text = getString(R.string.repository, data.repository)
        }
        Glide.with(this)
            .load(data.avatar)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_image_loading).error(R.drawable.ic_error))
            .into(binding.ivProfile)
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

    override fun onBackPressed() {
        super.onBackPressed()
        loadingDialog.dismissDialog()
        finish()
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