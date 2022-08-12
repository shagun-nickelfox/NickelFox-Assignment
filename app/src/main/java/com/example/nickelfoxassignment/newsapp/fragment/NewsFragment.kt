package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.FragmentNewsBinding
import com.example.nickelfoxassignment.newsapp.adapter.ArticleClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.MoreOptionsClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.NewsAdapter
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.newsapp.viewmodel.NewsViewModel
import com.example.nickelfoxassignment.shareData
import com.example.nickelfoxassignment.shortToast
import com.example.nickelfoxassignment.showPopUpMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.view.*

@AndroidEntryPoint
class NewsFragment : Fragment(), ArticleClickInterface,
    MoreOptionsClickInterface {

    private val viewModel by viewModels<NewsViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private val newsAdapter = NewsAdapter(this, this)
    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        setupChipListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.list.observe(viewLifecycleOwner) {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        newsAdapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading ->
                    view.progressBar.visibility = View.VISIBLE

                is LoadState.NotLoading ->
                    view.progressBar.visibility = View.GONE

                is LoadState.Error -> {
                    view.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.recycler_view.adapter = newsAdapter
    }

    private fun setupChipListener() {
        binding.apply {
            chipForYou.setOnClickListener {
                viewModel.setChipValue("ForYou")
                viewModel.setCategoryValue("")
            }
            chipTop.setOnClickListener {
                viewModel.setChipValue("Top")
                viewModel.setCategoryValue("")
            }
            chipBusiness.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("business")
            }
            chipEntertainment.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("entertainment")
            }
            chipHealth.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("health")
            }
            chipGeneral.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("general")
            }
            chipScience.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("science")
            }
            chipSports.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("sports")
            }
            chipTechnology.setOnClickListener {
                viewModel.setChipValue("")
                viewModel.setCategoryValue("technology")
            }
        }
    }

    override fun articleClick(bundle: Bundle) {
        findNavController().navigate(
            R.id.action_newsFragment_to_newsDetailFragment,
            bundle
        )
    }

    override fun moreOptionsClick(article: Article, time: String, view: View) {
        val popupMenu = activity?.showPopUpMenu(R.menu.item_menu, view)
        popupMenu?.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Share") {
                (activity as Context).shareData(
                    article.title!!,
                    article.url!!
                )
            } else {
                bookmarkViewModel.addBookmark(
                    Bookmark(
                        article.title,
                        article.author,
                        article.description,
                        article.source?.name,
                        article.urlToImage,
                        time,
                        article.url
                    )
                )
                (activity as Context).shortToast("Added to Bookmark")
            }
            true
        }
        popupMenu?.show()
    }
}