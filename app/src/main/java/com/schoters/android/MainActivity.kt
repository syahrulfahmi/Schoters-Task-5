package com.schoters.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.schoters.android.viewModel.NewsViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel : NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getNews()
    }
}