package com.example.nickelfoxassignment.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }
}