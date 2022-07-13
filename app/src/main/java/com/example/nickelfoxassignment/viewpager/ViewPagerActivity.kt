package com.example.nickelfoxassignment.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nickelfoxassignment.databinding.ActivityViewPagerBinding
import com.example.nickelfoxassignment.viewpager.viewpagerclasses.FirstScreen
import com.example.nickelfoxassignment.viewpager.viewpagerclasses.SecondScreen
import com.example.nickelfoxassignment.viewpager.viewpagerclasses.ThirdScreen
import com.example.nickelfoxassignment.viewpager.viewpagerclasses.ViewPagerAdapter

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
        if (binding.viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }
    }
}