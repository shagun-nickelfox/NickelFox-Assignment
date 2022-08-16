package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(), ArticleClickInterface,
    MoreOptionsClickInterface {

    private val viewModel by viewModels<NewsViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private val newsAdapter = NewsAdapter(this, this)
    private lateinit var binding: FragmentNewsBinding
    private var category = "ForYou"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        setupChipListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            newsAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerView.scrollToPosition(0) }
        }

        viewModel.list.observe(viewLifecycleOwner) {
            newsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        newsAdapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading -> {
                    view.progressBar.visibility = View.VISIBLE
                }

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
                viewModel.setChipValue("For You")
                viewModel.setCategoryValue("")
                category = "For You"
            }
            chipTop.setOnClickListener {
                viewModel.setChipValue("Top")
                viewModel.setCategoryValue("")
                category = "Top"
            }
            chipBusiness.setOnClickListener {
                viewModel.setChipValue("Business")
                viewModel.setCategoryValue("business")
                category = "Business"
            }
            chipEntertainment.setOnClickListener {
                viewModel.setChipValue("Entertainment")
                viewModel.setCategoryValue("entertainment")
                category = "Entertainment"
            }
            chipHealth.setOnClickListener {
                viewModel.setChipValue("Health")
                viewModel.setCategoryValue("health")
                category = "Health"
            }
            chipGeneral.setOnClickListener {
                viewModel.setChipValue("General")
                viewModel.setCategoryValue("general")
                category = "General"
            }
            chipScience.setOnClickListener {
                viewModel.setChipValue("Science")
                viewModel.setCategoryValue("science")
                category = "Science"
            }
            chipSports.setOnClickListener {
                viewModel.setChipValue("Sports")
                viewModel.setCategoryValue("sports")
                category = "Sports"
            }
            chipTechnology.setOnClickListener {
                viewModel.setChipValue("Technology")
                viewModel.setCategoryValue("technology")
                category = "Technology"
            }
        }
    }

    override fun articleClick(bundle: Bundle) {
        bundle.putString("category", category)
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
                        article.url,
                        category
                    )
                )
                (activity as Context).shortToast("Added to Bookmark")
            }
            true
        }
        popupMenu?.show()
    }
}