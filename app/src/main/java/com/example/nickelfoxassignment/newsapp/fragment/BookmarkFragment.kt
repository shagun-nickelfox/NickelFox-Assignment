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
import com.example.nickelfoxassignment.databinding.FragmentBookmarkBinding
import com.example.nickelfoxassignment.utils.shareData
import com.example.nickelfoxassignment.utils.shortToast
import com.example.nickelfoxassignment.utils.showPopUpMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(), ArticleClickInterface, MoreOptionsBookmarkClickInterface {

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel by viewModels<BookmarkViewModel>()
    private val bookmarkAdapter = BookmarkAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRv()
        setupChipListener()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.allBookmark.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                bookmarkAdapter.submitList(list)
            }
        }
    }

    private fun setupRv() {
        binding.recyclerView.adapter = bookmarkAdapter
    }

    private fun setupChipListener() {
        binding.apply {
            chipForYou.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.for_you))
            }
            chipTop.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.top))
            }
            chipBusiness.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.business))
            }
            chipEntertainment.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.entertainment))
            }
            chipHealth.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.health))
            }
            chipScience.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.science))
            }
            chipSports.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.sports))
            }
            chipTechnology.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.technology))
            }
            chipSearched.setOnClickListener {
                viewModel.setCategory(resources.getString(R.string.searched))
            }
        }
    }

    override fun articleClick(bundle: Bundle) {
        findNavController().navigate(
            R.id.action_bookmarkFragment_to_newsDetailFragment,
            bundle,
        )
    }

    override fun moreOptionsBookmarkClick(bookmark: Bookmark, view: View) {
        val popupMenu = showPopUpMenu(R.menu.bookmark_menu, view)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == resources.getString(R.string.share)) {
                (activity as Context).shareData(
                    bookmark.title!!,
                    bookmark.url!!
                )
            } else {
                viewModel.deleteBookmark(bookmark)
                (activity as Context).shortToast(resources.getString(R.string.remove_bookmark))
            }
            true
        }
        popupMenu.show()
    }
}