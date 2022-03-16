package com.example.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ItemRowUserBinding

class UserFollowersAdapter(private val followerList: ArrayList<User>): RecyclerView.Adapter<UserFollowersAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserFollowersAdapter.ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(var binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: UserFollowersAdapter.ListViewHolder, position: Int) {
        val (username, name, avatar) = followerList[position]
        holder.binding.tvUsername.text = username
        holder.binding.tvName.text = name
        Glide.with(holder.binding.root)
            .load(avatar)
            .into(holder.binding.imgItemPhoto)
    }

    override fun getItemCount(): Int = followerList.size


}