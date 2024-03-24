package com.dicoding.githubusersearch.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmarkEntity: BookmarkEntity)

    @Query("SELECT * FROM bookmark WHERE bookmarked = 1")
    fun getBookmarkedUser(): LiveData<List<BookmarkEntity>>

    @Query("DELETE FROM bookmark WHERE username = :username")
    fun deleteBookmarkedUser(username: String)

}