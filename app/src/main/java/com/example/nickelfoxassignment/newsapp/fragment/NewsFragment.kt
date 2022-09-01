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
import com.example.nickelfoxassignment.newsapp.adapter.*
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.newsapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import kotlinx.coroutines.flow.collectLatest
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
            viewModel.getTopHeadlines.collectLatest { pagingData ->
                newsAdapter.submitData(lifecycle, pagingData)
            }
        }

        newsAdapter.addLoadStateListener { state ->
            when (val currentState = state.refresh) {
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.GONE
                }
                is LoadState.Error -> {
                    progressBar.visibility = View.GONE
                    if (currentState.error.toString() == resources.getString(R.string.http_error) && !errorText)
                        context?.shortToast(getString(R.string.error))
                    errorText = true
                }
            }
        }

        view.recycler_view.apply {
            itemAnimator = null
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoaderAdapter { newsAdapter.retry() },
            )
        }
    }

    private fun setupChipListener() {
        binding.apply {
            swipeLayout.setOnRefreshListener {
                swipeLayout.isRefreshing = false
                newsAdapter.refresh()
                newsAdapter.submitData(lifecycle, emptyList)
                //progressBar.visibility = View.VISIBLE
            }
            chipForYou.setOnClickListener {
                setCategory(R.string.for_you, R.string.empty_string)
            }
            chipTop.setOnClickListener {
                setCategory(R.string.top, R.string.empty_string)
            }
            chipBusiness.setOnClickListener {
                setCategory(R.string.business, R.string.small_business)
            }
            chipEntertainment.setOnClickListener {
                setCategory(R.string.entertainment, R.string.small_entertainment)
            }
            chipHealth.setOnClickListener {
                setCategory(R.string.health, R.string.small_health)
            }
            chipScience.setOnClickListener {
                setCategory(R.string.science, R.string.small_science)
            }
            chipSports.setOnClickListener {
                setCategory(R.string.sports, R.string.small_sports)
            }
            chipTechnology.setOnClickListener {
                setCategory(R.string.technology, R.string.small_technology)
            }
        }
    }

    private fun setCategory(chip: Int, categoryValue: Int) {
        newsAdapter.submitData(viewLifecycleOwner.lifecycle, emptyList)
        viewModel.setChipValue(resources.getString(chip))
        viewModel.setCategoryValue(resources.getString(categoryValue))
        category = resources.getString(chip)
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