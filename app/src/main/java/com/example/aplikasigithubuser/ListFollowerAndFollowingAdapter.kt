package com.example.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ItemRowUserBinding

class ListFollowerAndFollowingAdapter(private val listUserDetail: ArrayList<UserList>) :
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
        val (username, avatar) = listUserDetail[position]
        holder.binding.tvUsername.text = username
        Glide.with(holder.binding.root)
            .load(avatar)
            .into(holder.binding.imgItemPhoto)
    }

    override fun getItemCount(): Int = listUserDetail.size

    inner class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

}