package com.example.aplikasigithubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUserDetail: ArrayList<UserList>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserList)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatar) = listUserDetail[position]
        holder.binding.tvUsername.text = username
        Glide.with(holder.binding.root)
            .load(avatar)
            .into(holder.binding.imgItemPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUserDetail[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUserDetail.size

}