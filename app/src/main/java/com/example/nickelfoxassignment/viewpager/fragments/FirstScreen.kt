package com.example.nickelfoxassignment.viewpager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.utils.shortToast
import com.example.nickelfoxassignment.databinding.FragmentFirstScreenBinding
import com.example.nickelfoxassignment.newsapp.NewsActivity

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            it.context.shortToast("Opening...")
            val intent = Intent(activity, NewsActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}