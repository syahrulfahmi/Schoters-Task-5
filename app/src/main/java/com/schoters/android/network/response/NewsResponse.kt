package com.schoters.android.network.response

import com.google.gson.annotations.SerializedName
import com.schoters.android.db.NewsEntity

data class NewsResponse(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("totalResults")
    val totalResults: Int = 0,
    @SerializedName("articles")
    val articles: List<NewsEntity>
)

data class News(
    @SerializedName("source") val source: Source,
    @SerializedName("author") val author: String? = "",
    @SerializedName("title") val title: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("url") val url: String? = "",
    @SerializedName("urlToImage") val urlToImage: String? = "",
    @SerializedName("publishedAt") val publishedAt: String? = "",
    @SerializedName("content") val content: String? = "",
    var isBookmarked: Int? = 0
)

data class Source(
    @SerializedName("id")
    val id: Any,
    @SerializedName("name")
    val name: String
)