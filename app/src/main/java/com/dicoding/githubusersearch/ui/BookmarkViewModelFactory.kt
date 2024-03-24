package com.dicoding.githubusersearch.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubusersearch.data.BookmarkRepository
import com.dicoding.githubusersearch.di.Injection

class BookmarkViewModelFactory private constructor(private val bookmarkRepository: BookmarkRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(bookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: BookmarkViewModelFactory? = null
        fun getInstance(context: Context): BookmarkViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: BookmarkViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}