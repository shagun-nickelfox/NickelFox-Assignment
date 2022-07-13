package com.example.nickelfoxassignment.viewpager.viewpagerclasses

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.databinding.FragmentThirdScreenBinding

class ThirdScreen : Fragment() {
    private lateinit var binding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding.tvThirdScreen.setOnClickListener {
            Toast.makeText(context, "Third Screen Clicked", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}

