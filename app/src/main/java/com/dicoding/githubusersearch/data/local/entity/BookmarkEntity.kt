package com.dicoding.githubusersearch.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bookmark")
class BookmarkEntity (
    @field:ColumnInfo(name = "username")
    @PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean? = null
) : Parcelable