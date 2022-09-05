package com.example.nickelfoxassignment.imageuploadapp.model

data class UploadResponse(
    val `data`: Data,
    val status: Int,
    val success: Boolean
)

data class Data(
    val account_id: Int,
    val account_url: Any,
    val ad_type: Int,
    val ad_url: String,
    val animated: Boolean,
    val bandwidth: Int,
    val datetime: Int,
    val deletehash: String,
    val description: Any,
    val favorite: Boolean,
    val height: Int,
    val id: String,
    val in_gallery: Boolean,
    val in_most_viral: Boolean,
    val is_ad: Boolean,
    val link: String,
    val name: String,
    val nsfw: Any,
    val section: Any,
    val size: Int,
    val tags: List<Any>,
    val title: Any,
    val type: String,
    val views: Int,
    val vote: Any,
    val width: Int
)