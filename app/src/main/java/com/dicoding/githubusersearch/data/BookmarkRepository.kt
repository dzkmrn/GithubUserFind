package com.dicoding.githubusersearch.data

import androidx.lifecycle.LiveData
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity
import com.dicoding.githubusersearch.data.local.room.BookmarkDao
import com.dicoding.githubusersearch.data.remote.retrofit.ApiService
import com.dicoding.githubusersearch.util.AppExecutors

class BookmarkRepository private constructor(
    private val apiService: ApiService,
    private val bookmarkDao: BookmarkDao,
    private val appExecutor: AppExecutors
) {

    fun getBookmarkedUser(): LiveData<List<BookmarkEntity>> {
        return bookmarkDao.getBookmarkedUser()
    }

    fun setBookmarkedUser(bookmark: BookmarkEntity, bookmarkState: Boolean) {
        appExecutor.diskIO.execute {
            bookmark.isBookmarked = bookmarkState
            bookmarkDao.insert(bookmark)
        }
    }

    fun deleteBookmarkedUser(username: String) {
        appExecutor.diskIO.execute {
            bookmarkDao.deleteBookmarkedUser(username)
        }
    }

    companion object {
        @Volatile
        private var instance: BookmarkRepository? = null
        fun getInstance(
            apiService: ApiService,
            bookmarkDao: BookmarkDao,
            appExecutor: AppExecutors
        ) : BookmarkRepository =
            instance ?: synchronized(this) {
                instance ?: BookmarkRepository(apiService, bookmarkDao, appExecutor)
            }.also { instance = it }
    }
}
