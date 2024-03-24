package com.dicoding.githubusersearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusersearch.R
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import com.dicoding.githubusersearch.databinding.ItemProfileBinding

class ProfileAdapter(private val bookmarkViewModel: BookmarkViewModel? = null, private val showBookmark: Boolean = true) : ListAdapter<ItemsItem, ProfileAdapter.MyViewHolder>(DIFF_CALLBACK){

    var onItemClick: ((String) -> Unit)? = null

    var onDeleteBookmarkClick: ((ItemsItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.MyViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.MyViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile)
    }

    inner class MyViewHolder(val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
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

                if (showBookmark == true) {
                    ivBookmark.visibility = View.VISIBLE
                } else if (showBookmark == false) {
                    ivBookmark.visibility = View.GONE
                }

                binding.ivBookmark.setOnClickListener {
                    onDeleteBookmarkClick?.invoke(getItem(adapterPosition))
                    if (profile.login != null) {
                        val isBookmarked = binding.ivBookmark.drawable.constantState == ContextCompat.getDrawable(itemView.context, R.drawable.baseline_bookmark_24)?.constantState
                        if (isBookmarked) {
                            bookmarkViewModel?.deleteUser(profile.login)
                        } else {
                            bookmarkViewModel?.saveUser(BookmarkEntity(profile.login, profile.avatarUrl))
                        }
                    }
                }

                bookmarkViewModel?.getBookmarkedUser()?.observe(itemView.context as LifecycleOwner) { bookmarkedItems ->
                    val isBookmarked = bookmarkedItems.any { it.username == profile.login }
                    val bookmarkIconResId = if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
                    binding.ivBookmark.setImageResource(bookmarkIconResId)
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