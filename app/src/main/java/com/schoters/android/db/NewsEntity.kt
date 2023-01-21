package com.schoters.android.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "news_entities")
data class NewsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("author") val author: String? = "",
    @ColumnInfo("title") val title: String? = "",
    @ColumnInfo("description") val description: String? = "",
    @ColumnInfo("url") val url: String? = "",
    @ColumnInfo("urlToImage") val urlToImage: String? = "",
    @ColumnInfo("publishedAt") val publishedAt: String? = "",
    @ColumnInfo("is_bookmark") var isBookmarked: Int? = 0,
    @ColumnInfo("type") var type: Int? = 0
) : Parcelable