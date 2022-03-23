package com.example.aplikasigithubuser

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ActivityDetailPageBinding
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

    private lateinit var binding: ActivityDetailPageBinding
    private val loadingDialog = LoadingDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(MainActivity.USER_DETAIL)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

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

        mainViewModel.userDetailDetail.observe(this) { userDetailData ->
            setUiText(userDetailData)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
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
            .into(binding.ivProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loadingDialog.startLoadingDialog()
        } else {
            loadingDialog.dismissDialog()
        }
    }
}