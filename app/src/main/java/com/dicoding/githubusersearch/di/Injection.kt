package com.dicoding.githubusersearch.di

import android.content.Context
import com.dicoding.githubusersearch.data.BookmarkRepository
import com.dicoding.githubusersearch.data.local.room.BookmarkDatabase
import com.dicoding.githubusersearch.data.remote.retrofit.ApiConfig
import com.dicoding.githubusersearch.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): BookmarkRepository {
        val apiService = ApiConfig.getApiService()
        val database = BookmarkDatabase.getInstance(context)
        val dao = database.bookmarkDao()
        val appExecutors = AppExecutors()
        return BookmarkRepository.getInstance(apiService, dao, appExecutors)
    }
}