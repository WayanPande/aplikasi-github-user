package com.example.aplikasigithubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null

    private lateinit var sharedViewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)


        sharedViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sharedViewModel.followingList.observe(viewLifecycleOwner) { data ->
            setFollowingList(data)
        }

        return binding.root
    }


    private fun setFollowingList(followerList: ArrayList<UserList>){
        binding.rvFollowing.setHasFixedSize(true)
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListFollowerAndFollowingAdapter(followerList)
        binding.rvFollowing.adapter = listUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}