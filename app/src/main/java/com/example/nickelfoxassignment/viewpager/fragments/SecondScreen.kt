package com.example.nickelfoxassignment.viewpager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nickelfoxassignment.databinding.FragmentSecondScreenBinding
import com.example.nickelfoxassignment.imageuploadapp.ImageUploadActivity
import com.example.nickelfoxassignment.utils.shortToast

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            it.context.shortToast("Opening...")
            val intent = Intent(activity, ImageUploadActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}