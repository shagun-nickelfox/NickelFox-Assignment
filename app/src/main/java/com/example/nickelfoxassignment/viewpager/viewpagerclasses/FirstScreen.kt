package com.example.nickelfoxassignment.viewpager.viewpagerclasses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.fragment_first_screen.view.*

class FirstScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_screen, container, false)

        view.text1.setOnClickListener{
            Toast.makeText(context,"First Screen CLicked",Toast.LENGTH_SHORT).show()
        }
        return view
    }
}