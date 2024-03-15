package com.dicoding.githubusersearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusersearch.data.response.ItemsItem
import com.dicoding.githubusersearch.databinding.ItemProfileBinding

class ProfileAdapter : ListAdapter<ItemsItem, ProfileAdapter.MyViewHolder>(DIFF_CALLBACK){

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.MyViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.MyViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile)
    }

    inner class MyViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ItemsItem) {
            with(binding) {
                tvItemName.text = profile.login
                Glide.with(itemView.context)
                    .load(profile.avatarUrl)
                    .into(imgItemPhoto)

                itemView.setOnClickListener {
                    onItemClick?.invoke(profile.login ?: "")
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("username", profile.login)
                    itemView.context.startActivity(intent)

                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}