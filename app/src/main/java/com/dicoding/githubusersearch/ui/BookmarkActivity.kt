package com.dicoding.githubusersearch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.dicoding.githubusersearch.R
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity
import com.dicoding.githubusersearch.data.remote.response.ItemsItem
import com.dicoding.githubusersearch.databinding.ActivityBookmarkBinding
import com.dicoding.githubusersearch.databinding.ActivityMainBinding
import com.dicoding.githubusersearch.databinding.ItemProfileBinding

class BookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookmarkBinding
    private var itemProfileBinding: ItemProfileBinding? = null
    private var bookmark: BookmarkEntity? = null
    private var profile: ItemsItem? = null

    private val bookmarkViewModel: BookmarkViewModel by viewModels {
        BookmarkViewModelFactory.getInstance(this)
    }
    private val profileAdapter = ProfileAdapter(showBookmark = true)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeBookmarkedUsers()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        binding.rvBookmark.adapter = profileAdapter

        bookmarkViewModel.getBookmarkedUser().observe(this) { bookmarkedItems ->
            val isBookmarked = itemProfileBinding?.ivBookmark?.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.baseline_bookmark_24)?.constantState
            if (isBookmarked) itemProfileBinding?.ivBookmark?.visibility = View.GONE
        }

        profileAdapter.onDeleteBookmarkClick = { profile ->
            profile.login.let { login ->
                if (login != null) {
                    bookmarkViewModel.deleteUser(login)
                }
            }
        }
    }

    private fun observeBookmarkedUsers() {
        bookmarkViewModel.getBookmarkedUser().observe(this, Observer { bookmarkedUsers ->
            binding.progressBar.visibility = View.GONE
            if (bookmarkedUsers.isEmpty()) {
                // Show empty message or placeholder layout
                binding.rvBookmark.visibility = View.GONE
            } else {
                val itemsList = bookmarkedUsers.map { bookmark ->
                    ItemsItem(login = bookmark.username, avatarUrl = bookmark.avatar)
                }
                profileAdapter.submitList(itemsList)
                Log.d("itemitemlist", "$itemsList")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
