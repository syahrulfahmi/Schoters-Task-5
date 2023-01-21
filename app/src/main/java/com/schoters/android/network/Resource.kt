package com.schoters.android.network

import com.schoters.android.utils.Data

sealed class Resource<T>(val data: T? = null, val message: String? = Data.EMPTY_STRING) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}