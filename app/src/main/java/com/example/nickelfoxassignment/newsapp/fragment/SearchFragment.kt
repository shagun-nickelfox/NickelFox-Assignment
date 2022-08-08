package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.nickelfoxassignment.newsapp.adapter.ArticleClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.MoreOptionsClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.NewsAdapter
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.retrofit.response.Article
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.FragmentSearchBinding
import com.example.nickelfoxassignment.newsapp.viewmodel.SearchViewModel
import com.example.nickelfoxassignment.shareData
import com.example.nickelfoxassignment.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_news.view.*

@AndroidEntryPoint
class SearchFragment : Fragment(), ArticleClickInterface,
    MoreOptionsClickInterface {

    private val viewModel by viewModels<SearchViewModel>()
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()
    private val newsAdapter = NewsAdapter(this, this)
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.searchList.observe(viewLifecycleOwner) {
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
                    //activity?.shortToast("Error in fetching data")
                }
            }
        }
        view.recycler_view.adapter = newsAdapter
    }

    private fun setupListeners() {
        binding.apply {
            tvSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.setSearchValue(tvSearch.text.toString())
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    override fun articleClick(bundle: Bundle) {
        findNavController().navigate(
            R.id.action_searchFragment_to_newsDetailFragment,
            bundle
        )
    }

    override fun moreOptionsClick(article: Article, time: String) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
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
        popupMenu.show()
    }
}