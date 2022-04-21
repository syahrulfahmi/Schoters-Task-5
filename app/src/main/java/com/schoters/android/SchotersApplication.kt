package com.schoters.android

import android.app.Application
import com.schoters.android.db.AppDatabase

class SchotersApplication: Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}