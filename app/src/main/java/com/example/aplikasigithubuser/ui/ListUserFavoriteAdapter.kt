package com.example.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.UserList
import com.example.aplikasigithubuser.data.local.entity.UserEntity
import com.example.aplikasigithubuser.databinding.ItemRowUserBinding

class ListUserFavoriteAdapter(private val listUserFavorite: ArrayList<UserList>) :  ListAdapter<UserEntity, ListUserFavoriteAdapter.ListViewHolder>(
    DIFF_CALLBACK
){

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserList)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatar) = listUserFavorite[position]
        holder.binding.tvUsername.text = username
        Glide.with(holder.binding.root)
            .load(avatar)
            .apply(RequestOptions.placeholderOf(R.drawable.ic_image_loading).error(R.drawable.ic_error))
            .into(holder.binding.imgItemPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUserFavorite[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUserFavorite.size

     class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}