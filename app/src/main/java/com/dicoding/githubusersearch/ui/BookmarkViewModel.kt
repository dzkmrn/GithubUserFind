package com.dicoding.githubusersearch.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.githubusersearch.data.BookmarkRepository
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity

class BookmarkViewModel(private val bookmarkRepository: BookmarkRepository) : ViewModel() {
    fun getBookmarkedUser() = bookmarkRepository.getBookmarkedUser()

    fun saveUser(bookmark: BookmarkEntity) {
        bookmarkRepository.setBookmarkedUser(bookmark, true)
        Log.d("BookmarkViewModel", "User saved: $bookmark")
    }

    fun deleteUser(username: String) {
        bookmarkRepository.deleteBookmarkedUser(username)
        Log.d("BookmarkViewModel", "User delete: $username")
    }
}