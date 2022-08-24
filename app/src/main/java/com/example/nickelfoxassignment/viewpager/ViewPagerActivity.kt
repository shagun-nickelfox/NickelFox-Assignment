package com.example.nickelfoxassignment.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.paging.ExperimentalPagingApi
import com.example.nickelfoxassignment.databinding.ActivityViewPagerBinding
import com.example.nickelfoxassignment.viewpager.fragments.FirstScreen
import com.example.nickelfoxassignment.viewpager.fragments.SecondScreen
import com.example.nickelfoxassignment.viewpager.fragments.ThirdScreen
import com.example.nickelfoxassignment.viewpager.fragments.ViewPagerAdapter

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupFragment()
    }

    private fun setupFragment() {
        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        binding.apply {
            if (viewPager.currentItem == 0) {
                super.onBackPressed()
            } else {
                viewPager.currentItem = viewPager.currentItem - 1
            }
        }
    }
}