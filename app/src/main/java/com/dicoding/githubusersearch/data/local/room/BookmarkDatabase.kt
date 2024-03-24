package com.dicoding.githubusersearch.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubusersearch.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var instance: BookmarkDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): BookmarkDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java, "bookmark.db"
                ).build()
            }
    }

}