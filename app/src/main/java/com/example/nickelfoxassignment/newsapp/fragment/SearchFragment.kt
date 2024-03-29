package com.example.nickelfoxassignment.newsapp.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nickelfoxassignment.newsapp.adapter.ArticleClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.MoreOptionsClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.NewsAdapter
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.FragmentSearchBinding
import com.example.nickelfoxassignment.newsapp.adapter.ArticlesLoadStateAdapter
import com.example.nickelfoxassignment.newsapp.viewmodel.SearchViewModel
import com.example.nickelfoxassignment.utils.shareData
import com.example.nickelfoxassignment.utils.shortToast
import com.example.nickelfoxassignment.utils.showPopUpMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class SearchFragment : Fragment(), ArticleClickInterface,
    MoreOptionsClickInterface {
    private val viewModel by viewModels<SearchViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var footer: ArticlesLoadStateAdapter
    private lateinit var header: ArticlesLoadStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRV()
        setupListeners()
        setupObservers()
    }

    private fun setupRV() {
        binding.apply {
            newsAdapter = NewsAdapter(this@SearchFragment, this@SearchFragment)
            header = ArticlesLoadStateAdapter { newsAdapter.refresh() }
            footer = ArticlesLoadStateAdapter { newsAdapter.retry() }

            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = newsAdapter.withLoadStateHeaderAndFooter(
                footer = footer,
                header = header
            )
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.searchList.collectLatest {
                newsAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            newsAdapter.addLoadStateListener { state ->
                header.loadState = state.refresh
                recyclerView.isVisible = state.refresh !is LoadState.Loading
                when (val currentState = state.refresh) {
                    is LoadState.Loading ->
                        progressBar.visibility = View.VISIBLE

                    is LoadState.NotLoading ->
                        progressBar.visibility = View.GONE

                    is LoadState.Error -> {
                        progressBar.visibility = View.GONE
                        if (currentState.error.toString() == resources.getString(R.string.net_error))
                            context?.shortToast(getString(R.string.internet_error))
                        else if (currentState.error.toString() == resources.getString(R.string.retrofit_error))
                            context?.shortToast(getString(R.string.api_error))
                    }
                }
            }
            tvSearch.apply {
                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus)
                        activity?.showKeyboard(v)
                }
                setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        newsAdapter.submitData(lifecycle, PagingData.empty())
                        activity?.hideKeyboard(v)
                        viewModel.setSearchValue(tvSearch.text.toString())
                        return@OnEditorActionListener true
                    }
                    false
                })
                requestFocus()
            }

            textInputLayout.setEndIconOnClickListener {
                tvSearch.text?.clear()
            }
        }
    }

    override fun articleClick(bundle: Bundle) {
        bundle.putString("category", resources.getString(R.string.searched))
        findNavController().navigate(
            R.id.action_searchFragment_to_newsDetailFragment,
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
                                resources.getString(R.string.searched),
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

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

private fun Context.showKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}
