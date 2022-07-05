package com.example.nickelfoxassignment.viewpager.viewpagerClasses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nickelfoxassignment.databinding.FragmentSecondScreenBinding

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        binding.tvSecondScreen.setOnClickListener {
            Toast.makeText(context, "Second Screen Clicked", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}