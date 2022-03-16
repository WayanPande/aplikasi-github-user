package com.example.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ItemRowUserBinding

class ListFollowerAndFollowingAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListFollowerAndFollowingAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val (username, name, avatar) = listUser[position]
        holder.binding.tvUsername.text = username
        holder.binding.tvName.text = name
        Glide.with(holder.binding.root)
            .load(avatar)
            .into(holder.binding.imgItemPhoto)
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

}