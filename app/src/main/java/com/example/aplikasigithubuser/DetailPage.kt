package com.example.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasigithubuser.databinding.ActivityDetailPageBinding

class DetailPage : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<User>("DATA")

        binding.tvDetailName.text = data?.name
        data?.avatar?.let { binding.ivProfile.setImageResource(it) }
        binding.tvDetailUsername.text = data?.username
        binding.tvFollowers.text = data?.followers
        binding.tvFollowing.text = data?.following
        binding.tvCompany.text = data?.company
        binding.tvLocation.text = data?.location
        binding.tvRepository.text = getString(R.string.repository, data?.repository)
    }
}