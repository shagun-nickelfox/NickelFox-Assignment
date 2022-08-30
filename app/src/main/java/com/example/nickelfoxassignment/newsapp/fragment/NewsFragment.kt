package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        viewModel.getTopHeadlines.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
                newsAdapter.submitData(lifecycle, pagingData)
                errorText = false
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
                    if (currentState.error.toString() == resources.getString(R.string.http_error) && !errorText)
                        context?.shortToast(getString(R.string.error))
                    else if (currentState.error.toString() == resources.getString(R.string.data_fetch_error) && !errorText) {
                        context?.shortToast(getString(R.string.internet_error))
                    }
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
                swipeLayout.isRefreshing = false
                newsAdapter.refresh()
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                progressBar.visibility = View.VISIBLE
            }
            chipForYou.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.for_you))
                viewModel.setCategoryValue(resources.getString(R.string.empty_string))
                category = resources.getString(R.string.for_you)
            }
            chipTop.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.top))
                viewModel.setCategoryValue(resources.getString(R.string.empty_string))
                category = resources.getString(R.string.top)
            }
            chipBusiness.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.business))
                viewModel.setCategoryValue(resources.getString(R.string.small_business))
                category = resources.getString(R.string.business)
            }
            chipEntertainment.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.entertainment))
                viewModel.setCategoryValue(resources.getString(R.string.small_entertainment))
                category = resources.getString(R.string.entertainment)
            }
            chipHealth.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.health))
                viewModel.setCategoryValue(resources.getString(R.string.small_health))
                category = resources.getString(R.string.health)
            }
            chipScience.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.science))
                viewModel.setCategoryValue(resources.getString(R.string.small_science))
                category = resources.getString(R.string.science)
            }
            chipSports.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.sports))
                viewModel.setCategoryValue(resources.getString(R.string.small_sports))
                category = resources.getString(R.string.sports)
            }
            chipTechnology.setOnClickListener {
                newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
                viewModel.setChipValue(resources.getString(R.string.technology))
                viewModel.setCategoryValue(resources.getString(R.string.small_technology))
                category = resources.getString(R.string.technology)
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
            if (menuItem.title == resources.getString(R.string.share)) {
                article.url?.let {
                    (activity as Context).shareData(
                        article.title,
                        it
                    )
                }
            } else {
                bookmarkViewModel.exists(
                    article.author,
                    article.title,
                    article.source.name
                ).observe(viewLifecycleOwner) { exists ->
                    if (exists) {
                        (activity as Context).shortToast(resources.getString(R.string.already_added_bookmark))
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
                                category,
                                article.id
                            )
                        )
                        (activity as Context).shortToast(resources.getString(R.string.added_bookmark))
                    }
                }
            }
            true
        }
        popupMenu.show()
    }
}