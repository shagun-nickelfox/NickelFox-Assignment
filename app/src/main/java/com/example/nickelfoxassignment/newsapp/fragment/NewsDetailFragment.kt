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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)

        binding.apply {
            tvHeadlineDetail.text = requireArguments()["title"] as String?
            tvAuthor.text = requireArguments()["author"] as String?
            tvContent.text = requireArguments()["description"] as String?
            tvTime.text = requireArguments()["time"] as String?
            Glide.with(binding.root).load(requireArguments()["image"])
                .into(ivHeadlineDetail)
        }
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun setupListeners() {
        binding.apply {
            ivBookmark.setOnClickListener {
                viewModel.exists(
                    tvAuthor.text.toString(),
                    tvHeadlineDetail.text.toString(),
                    requireArguments()["source"].toString()
                ).observe(viewLifecycleOwner) { exists ->
                    if (exists) {
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
                        (activity as Context).shortToast(resources.getString(R.string.added_bookmark))
                    }
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