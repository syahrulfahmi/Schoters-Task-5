package com.schoters.android.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.schoters.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    companion object {

        fun httpClient(context: Context): OkHttpClient {
            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                builder.apply {
                    addInterceptor(logInterceptor)
                    addInterceptor(
                        ChuckerInterceptor.Builder(context)
                            .collector(ChuckerCollector(context))
                            .maxContentLength(250000L)
                            .redactHeaders(emptySet())
                            .alwaysReadResponseBody(false)
                            .build()
                    )
                }
            }
            return builder.build()
        }

        inline fun <reified T> newInstance(okHttpClient: OkHttpClient): T {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(T::class.java)
        }
    }
}