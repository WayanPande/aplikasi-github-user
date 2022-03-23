package com.example.aplikasigithubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowersAndFollowingBinding

class FollowersAndFollowingFragment(private val type: String) : Fragment() {

    companion object {
        const val FOLLOWERS_TYPE = "followers_type"
        const val FOLLOWING_TYPE = "following_type"
    }

    private var _binding: FragmentFollowersAndFollowingBinding? = null

    private lateinit var sharedViewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFollowersAndFollowingBinding.inflate(inflater, container, false)


        sharedViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        when (type) {
            FOLLOWERS_TYPE -> {
                sharedViewModel.followerList.observe(viewLifecycleOwner) { data ->
                    setUserList(data)
                }
            }
            FOLLOWING_TYPE -> {
                sharedViewModel.followingList.observe(viewLifecycleOwner) { data ->
                    setUserList(data)
                }
            }
        }

        return binding.root
    }


    private fun setUserList(userList: ArrayList<UserList>){
        binding.rvFollowersAndFollowing.setHasFixedSize(true)
        binding.rvFollowersAndFollowing.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListFollowerAndFollowingAdapter(userList)
        binding.rvFollowersAndFollowing.adapter = listUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}