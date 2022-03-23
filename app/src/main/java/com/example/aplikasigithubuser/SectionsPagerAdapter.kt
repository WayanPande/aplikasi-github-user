package com.example.aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position){
            0 -> fragment = FollowersAndFollowingFragment(FollowersAndFollowingFragment.FOLLOWERS_TYPE)
            1 -> fragment = FollowersAndFollowingFragment(FollowersAndFollowingFragment.FOLLOWING_TYPE)
        }
        return fragment as Fragment
    }

}