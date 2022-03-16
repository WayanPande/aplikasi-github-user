package com.example.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ActivityDetailPageBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private lateinit var binding: ActivityDetailPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(MainActivity.USER_DETAIL)

        if (username != null) {
            findUserDetail(username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        if (username != null) {
            mainViewModel.apply {
                getFollowersOrFollowingData(username, MainViewModel.FOLLOWERS)
                getFollowersOrFollowingData(username, MainViewModel.FOLLOWING)
            }
        }
    }

    private fun findUserDetail(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val userData = User(
                            responseBody.login!!,
                            responseBody.name ?: responseBody.login,
                            responseBody.avatarUrl!!,
                            responseBody.location ?: "-",
                            responseBody.publicRepos!!.toString(),
                            responseBody.company ?: "-",
                            responseBody.followers!!.toString(),
                            responseBody.following!!.toString()
                        )
                        setUiText(userData)
                    }
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
            }
        })

    }


    private fun setUiText(data: User) {

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
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}