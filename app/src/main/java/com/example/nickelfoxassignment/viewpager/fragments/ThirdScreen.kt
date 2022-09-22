package com.example.nickelfoxassignment.viewpager.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.databinding.FragmentThirdScreenBinding
import com.example.nickelfoxassignment.stopwatch.StopwatchActivity

class ThirdScreen : Fragment() {
    private lateinit var binding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            val intent = Intent(activity, StopwatchActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}

