package com.schoters.android.network.field

data class NewsField(
    var page: Int = 1,
    var perPage: Int = 10,
    var total: Int = 0
)