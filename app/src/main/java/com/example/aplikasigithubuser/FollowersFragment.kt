package com.example.aplikasigithubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null

    private lateinit var sharedViewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFollowersBinding.inflate(inflater, container, false)


        sharedViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sharedViewModel.followerList.observe(viewLifecycleOwner) { data ->
            setFollowerList(data)
        }

        return binding.root
    }


    private fun setFollowerList(followerList: ArrayList<UserList>){
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListFollowerAndFollowingAdapter(followerList)
        binding.rvFollowers.adapter = listUserAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}