package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nickelfoxassignment.*
import com.example.nickelfoxassignment.databinding.FragmentNewsBinding
import com.example.nickelfoxassignment.newsapp.adapter.ArticleClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.MoreOptionsClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.NewsAdapter
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class NewsFragment : Fragment(), ArticleClickInterface,
    MoreOptionsClickInterface {

    private val viewModel by viewModels<NewsViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private val newsAdapter = NewsAdapter(this, this)
    private lateinit var binding: FragmentNewsBinding
    private var category = "For You"
    private val emptyList: PagingData<Article> = PagingData.empty()
    private var errorText = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        setupChipListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getTopHeadlines.observe(viewLifecycleOwner) { pagingData ->
                pagingData?.let {
                    newsAdapter.submitData(lifecycle, pagingData)
                    errorText = false
                }
            }
        }

        newsAdapter.addLoadStateListener { state ->
            when (val currentState = state.refresh) {
                is LoadState.Loading -> {
                    view.progressBar.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    view.progressBar.visibility = View.GONE
                }
                is LoadState.Error -> {
                    view.progressBar.visibility = View.GONE
                    if (currentState.error.toString() == "retrofit2.HttpException: HTTP 429 " && !errorText)
                        context?.getString(R.string.error)?.let { activity?.longToast(it) }
                    errorText = true
                }
            }
        }
        view.recycler_view.apply {
            itemAnimator = null
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
    }

    private fun setupChipListener() {
        binding.apply {
            swipeLayout.setOnRefreshListener {
                newsAdapter.refresh()
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                swipeLayout.isRefreshing = false
            }
            chipForYou.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("For You")
                viewModel.setCategoryValue("")
                category = "For You"
            }
            chipTop.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Top")
                viewModel.setCategoryValue("")
                category = "Top"
            }
            chipBusiness.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Business")
                viewModel.setCategoryValue("business")
                category = "Business"
            }
            chipEntertainment.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Entertainment")
                viewModel.setCategoryValue("entertainment")
                category = "Entertainment"
            }
            chipHealth.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Health")
                viewModel.setCategoryValue("health")
                category = "Health"
            }
            chipScience.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Science")
                viewModel.setCategoryValue("science")
                category = "Science"
            }
            chipSports.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue("Sports")
                viewModel.setCategoryValue("sports")
                category = "Sports"
            }
            chipTechnology.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
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
        val popupMenu = showPopUpMenu(R.menu.item_menu, view)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Share") {
                article.url?.let {
                    (activity as Context).shareData(
                        article.title,
                        it
                    )
                }
            } else {
                bookmarkViewModel.addBookmark(
                    Bookmark(
                        article.title,
                        article.author,
                        article.description,
                        article.source.name,
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
        popupMenu.show()
    }
}