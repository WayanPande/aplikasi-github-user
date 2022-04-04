package com.example.aplikasigithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.UserList
import com.example.aplikasigithubuser.databinding.FragmentFollowersAndFollowingBinding

class FollowersAndFollowingFragment : Fragment() {

    private var _binding: FragmentFollowersAndFollowingBinding? = null

    private lateinit var sharedViewModel: MainViewModel

    private val binding get() = _binding!!

    companion object {
        const val FOLLOWERS = "followers"
        const val FOLLOWING = "following"
        const val ARG_TAB = "argTab"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFollowersAndFollowingBinding.inflate(inflater, container, false)

        val tabType = arguments?.getString(ARG_TAB)


        sharedViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        if (tabType == FOLLOWERS) {
            sharedViewModel.followerList.observe(viewLifecycleOwner) { data ->
                setFollowerAndFollowingList(data)
            }
        }else if (tabType == FOLLOWING) {
            sharedViewModel.followingList.observe(viewLifecycleOwner) { data ->
                setFollowerAndFollowingList(data)
            }
        }

        return binding.root
    }


    private fun setFollowerAndFollowingList(followerList: ArrayList<UserList>){
        binding.rvFollowersFollowing.setHasFixedSize(true)
        binding.rvFollowersFollowing.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListFollowerAndFollowingAdapter(followerList)
        binding.rvFollowersFollowing.adapter = listUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}