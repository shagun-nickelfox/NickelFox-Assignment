package com.example.nickelfoxassignment.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.R

class ViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)

        supportActionBar?.hide()
    }
}