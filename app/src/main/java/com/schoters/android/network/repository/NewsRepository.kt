package com.schoters.android.network.repository

import com.schoters.android.network.datasource.NewsDataSource
import com.schoters.android.network.response.NewsResponse
import javax.inject.Singleton

interface NewsRepository {
    suspend fun getNews(category: String): NewsResponse?
}

@Singleton
class NewsRepositoryImpl(private val dataSource: NewsDataSource) : NewsRepository {
    override suspend fun getNews(category: String): NewsResponse? {
        val response = dataSource.getNews(category)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception(response.message())
        }
    }
}