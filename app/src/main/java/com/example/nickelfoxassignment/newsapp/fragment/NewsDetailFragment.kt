package com.example.nickelfoxassignment.newsapp.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.newsapp.database.Bookmark
import com.example.nickelfoxassignment.newsapp.viewmodel.BookmarkViewModel
import com.example.nickelfoxassignment.databinding.FragmentNewsDetailBinding
import com.example.nickelfoxassignment.shareData
import com.example.nickelfoxassignment.shortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val viewModel by viewModels<BookmarkViewModel>()
    private var articleExists = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkArticleInBookmark()
        setupListeners()
    }

    private fun checkArticleInBookmark() {
        binding.apply {
            viewModel.exists(
                requireArguments()["author"] as String,
                requireArguments()["title"] as String,
                requireArguments()["source"].toString()
            ).observe(viewLifecycleOwner) { exists ->
                if (exists) {
                    ivBookmark.setImageResource(R.drawable.bookmark_selected)
                    articleExists = true
                }
            }
            articleExists = false
        }
    }

    private fun setupListeners() {
        binding.apply {
            tvHeadlineDetail.text = requireArguments()["title"] as String?
            tvAuthor.text = requireArguments()["author"] as String?
            tvContent.text = requireArguments()["description"] as String?
            tvTime.text = requireArguments()["time"] as String?
            Glide.with(binding.root).load(requireArguments()["image"])
                .into(ivHeadlineDetail)

            ivBookmark.setOnClickListener {
                if (articleExists) {
                    (activity as Context).shortToast(resources.getString(R.string.already_added_bookmark))
                } else {
                    viewModel.addBookmark(
                        Bookmark(
                            tvHeadlineDetail.text.toString(),
                            tvAuthor.text.toString(),
                            tvContent.text.toString(),
                            requireArguments()["source"].toString(),
                            requireArguments()["image"].toString(),
                            tvTime.text.toString(),
                            "",
                            requireArguments()["category"].toString(),
                            requireArguments()["id"].toString()
                        )
                    )
                    articleExists = true
                    (activity as Context).shortToast(resources.getString(R.string.added_bookmark))
                    ivBookmark.setImageResource(R.drawable.bookmark_selected)
                }
            }
            ivShare.setOnClickListener {
                (activity as Context).shareData(
                    tvHeadlineDetail.text.toString(),
                    requireArguments()["url"].toString()
                )
            }

            ivBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }
}