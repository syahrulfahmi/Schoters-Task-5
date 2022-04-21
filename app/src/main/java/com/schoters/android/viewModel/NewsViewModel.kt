package com.schoters.android.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.schoters.android.BuildConfig
import com.schoters.android.db.NewsDao
import com.schoters.android.db.NewsEntity
import com.schoters.android.network.ApiService
import com.schoters.android.network.datasource.NewsDataSource
import com.schoters.android.network.field.NewsField
import com.schoters.android.network.response.NewsResponse
import com.schoters.android.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(application: Application, private val newsDao: NewsDao) : AndroidViewModel(application) {
    val newsResponse = MutableLiveData<NewsResponse?>()
    var category = Constant.HEALTH
    val newsField = NewsField()
    var isLastPage = false

    fun getNews() {
        viewModelScope.launch {
            val apiEndpoint = ApiService.newInstance<NewsDataSource>(ApiService.httpClient(getApplication<Application>().applicationContext))
            val call = apiEndpoint.getNews(category, BuildConfig.API_KEY, newsField.page, newsField.perPage)
            call.enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        newsResponse.value = response.body()
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    newsResponse.value = null
                }
            })
        }
    }

    fun insertNews(news: NewsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.insertNews(news)
        }
    }

    fun saveBookmark(news: NewsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.updateNews(news)
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class NewsViewModelFactory(private val application: Application, private val itemDao: NewsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(application, itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}