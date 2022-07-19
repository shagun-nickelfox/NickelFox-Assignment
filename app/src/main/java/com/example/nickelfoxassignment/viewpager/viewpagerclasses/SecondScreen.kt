package com.example.nickelfoxassignment.viewpager.viewpagerclasses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nickelfoxassignment.databinding.FragmentSecondScreenBinding
import com.example.nickelfoxassignment.shortToast

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        binding.tvSecondScreen.setOnClickListener {
            it.context.shortToast("Second Screen Clicked")
        }
        return binding.root
    }
}