package com.schoters.android.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.schoters.android.BuildConfig
import com.schoters.android.network.ApiService
import com.schoters.android.network.datasource.NewsDataSource
import com.schoters.android.network.response.NewsResponse
import com.schoters.android.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    val newsResponse = MutableLiveData<NewsResponse>()
    var isProcess = MutableLiveData<Boolean>()
    var search = Constant.TECH

    fun getNews() {
        isProcess.value = true
        val apiEndpoint = ApiService.newInstance<NewsDataSource>(ApiService.httpClient(getApplication<Application>().applicationContext))
        val call = apiEndpoint.getNews(search, BuildConfig.API_KEY)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    newsResponse.value = response.body()
                    isProcess.value = false
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.i("onFailure", t.toString())
                isProcess.value = false
            }
        })
    }
}