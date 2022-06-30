package com.example.nickelfoxassignment.viewpager.viewpagerclasses

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nickelfoxassignment.R
import kotlinx.android.synthetic.main.fragment_third_screen.view.*


class ThirdScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_screen, container, false)

        view.text3.setOnClickListener{
            Toast.makeText(context,"Third Screen CLicked", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}

