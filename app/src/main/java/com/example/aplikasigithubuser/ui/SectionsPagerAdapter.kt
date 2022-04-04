package com.example.aplikasigithubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersAndFollowingFragment()
        val bundle = Bundle()
        when (position){
            0 -> bundle.putString(FollowersAndFollowingFragment.ARG_TAB, FollowersAndFollowingFragment.FOLLOWERS)
            1 -> bundle.putString(FollowersAndFollowingFragment.ARG_TAB, FollowersAndFollowingFragment.FOLLOWING)
        }
        fragment.arguments = bundle
        return fragment
    }

}