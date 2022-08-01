package com.example.nickelfoxassignment.viewpager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.shortToast
import com.example.nickelfoxassignment.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)

        binding.tvFirstScreen.setOnClickListener {
            it.context.shortToast("First Screen Clicked")
        }
        return binding.root
    }
}