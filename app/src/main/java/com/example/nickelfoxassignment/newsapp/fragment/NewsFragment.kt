package com.example.nickelfoxassignment.newsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.nickelfoxassignment.newsapp.adapter.NewsAdapter
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.newsapp.helper.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.view.*

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val viewModel by viewModels<NewsViewModel> ()
    private val newsAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.list.observe(viewLifecycleOwner){
            newsAdapter.submitData(lifecycle,it)
        }

        newsAdapter.addLoadStateListener { state ->
            when(state.refresh){
                is LoadState.Loading ->
                    view.progressBar.visibility = View.VISIBLE

                is LoadState.NotLoading ->
                    view.progressBar.visibility = View.GONE

                is LoadState.Error -> {
                    view.progressBar.visibility = View.GONE
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.recycler_view.adapter = newsAdapter
    }

}