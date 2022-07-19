package com.example.nickelfoxassignment.viewpager.viewpagerclasses

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.databinding.FragmentThirdScreenBinding
import com.example.nickelfoxassignment.shortToast

class ThirdScreen : Fragment() {
    private lateinit var binding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        binding.tvThirdScreen.setOnClickListener {
            it.context.shortToast("Third Screen Clicked")
        }
        return binding.root
    }
}

