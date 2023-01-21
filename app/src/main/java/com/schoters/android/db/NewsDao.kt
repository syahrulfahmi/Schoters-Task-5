package com.schoters.android.db

import androidx.room.*

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_entities")
    fun getAllNews(): List<NewsEntity>

    @Query("SELECT * FROM news_entities WHERE is_bookmark = 1")
    fun getBookmarkNews(): List<NewsEntity>

    @Update
    fun updateNews(newsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(newsEntity: List<NewsEntity>)

}