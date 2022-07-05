package com.example.nickelfoxassignment.viewpager.viewpagerClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)

        binding.tvFirstScreen.setOnClickListener {
            Toast.makeText(context, "First Screen Clicked", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}