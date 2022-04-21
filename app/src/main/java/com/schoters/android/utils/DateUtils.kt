package com.schoters.android.utils

import java.text.SimpleDateFormat
import java.util.*


fun String.convertTo(format: String) : String {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    val newDate: Date? = dateFormat.parse(this)
    dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    return dateFormat.format(newDate ?: "")
}