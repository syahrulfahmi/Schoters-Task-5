package com.schoters.android.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schoters.android.db.NewsDao
import com.schoters.android.db.NewsEntity
import com.schoters.android.network.Resource
import com.schoters.android.network.repository.NewsRepository
import com.schoters.android.network.response.NewsResponse
import com.schoters.android.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsDao: NewsDao, private val repository: NewsRepository) : ViewModel() {

    private val _newsResponse = MutableLiveData<Resource<NewsResponse>>()
    val newsResponse: LiveData<Resource<NewsResponse>> = _newsResponse

    var category = Constant.HEALTH

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            _newsResponse.value = Resource.Loading()
            try {
                val response = repository.getNews(category)
                _newsResponse.value = Resource.Success(data = response)
            } catch (e: Exception) {
                _newsResponse.value = Resource.Error(message = e.message)
            }
        }
    }

    fun updateBookmark(news: NewsEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.updateNews(news)
        }
    }

    fun getBookmarkedNews(onSuccess: (List<NewsEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onSuccess(newsDao.getBookmarkNews())
        }
    }

    fun saveAllNews(news: List<NewsEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            newsDao.insertNews(news)
        }
    }
}