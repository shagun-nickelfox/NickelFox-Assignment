package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nickelfoxassignment.newsapp.adapter.ArticleClickInterface
import com.example.nickelfoxassignment.newsapp.adapter.BookmarkAdapter
import com.example.nickelfoxassignment.newsapp.adapter.MoreOptionsBookmarkClickInterface
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.shareData
import com.example.nickelfoxassignment.shortToast
import com.example.nickelfoxassignment.showPopUpMenu
import kotlinx.android.synthetic.main.fragment_news.view.*

class BookmarkFragment : Fragment(), ArticleClickInterface, MoreOptionsBookmarkClickInterface {

    private val viewModel by viewModels<BookmarkViewModel>()
    private val bookmarkAdapter = BookmarkAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.recycler_view.adapter = bookmarkAdapter

        viewModel.allBookmarkNews.observe(viewLifecycleOwner) { list ->
            list?.let {
                bookmarkAdapter.updateList(it)
            }
        }
    }

    override fun articleClick(bundle: Bundle) {
        findNavController().navigate(
            R.id.action_bookmarkFragment_to_newsDetailFragment,
            bundle
        )
    }

    override fun moreOptionsBookmarkClick(bookmark: Bookmark, view: View) {
        val popupMenu = activity?.showPopUpMenu(R.menu.bookmark_menu, view)
        popupMenu?.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Share") {
                (activity as Context).shareData(
                    bookmark.title!!,
                    bookmark.url!!
                )
            } else {
                viewModel.deleteBookmark(bookmark)
                (activity as Context).shortToast("Remove from Bookmark")
            }
            true
        }
        popupMenu?.show()
    }
}