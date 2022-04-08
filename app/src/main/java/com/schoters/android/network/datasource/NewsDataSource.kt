package com.schoters.android.network.datasource

import com.schoters.android.network.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * request rate limit 100/day, be careful to use this api
 */
interface NewsDataSource {
    @GET("everything")
    fun getNews(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}
