package com.example.nickelfoxassignment.newsapp.retrofit.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
) : Parcelable