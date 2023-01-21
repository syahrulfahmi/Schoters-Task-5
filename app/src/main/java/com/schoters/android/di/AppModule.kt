package com.schoters.android.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.schoters.android.BuildConfig
import com.schoters.android.db.AppDatabase
import com.schoters.android.db.NewsDao
import com.schoters.android.network.datasource.NewsDataSource
import com.schoters.android.network.repository.NewsRepository
import com.schoters.android.network.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
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

    @Provides
    @Singleton
    fun provideRetrofitService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao = appDatabase.newsDao()

    @Provides
    fun provideService(retrofitClient: Retrofit): NewsDataSource = retrofitClient.create(NewsDataSource::class.java)

    @Provides
    @Singleton
    fun provideNewsRepository(datasource: NewsDataSource): NewsRepository = NewsRepositoryImpl(datasource)
}