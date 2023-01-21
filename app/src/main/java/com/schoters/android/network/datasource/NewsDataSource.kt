package com.schoters.android.network.datasource

import com.schoters.android.BuildConfig
import com.schoters.android.network.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * request rate limit 100/day, be careful to use this api
 */
interface NewsDataSource {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("country") country: String = "id",
    ): Response<NewsResponse>
}
