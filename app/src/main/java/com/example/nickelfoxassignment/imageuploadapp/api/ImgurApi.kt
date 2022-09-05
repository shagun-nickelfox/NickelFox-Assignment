package com.example.nickelfoxassignment.imageuploadapp.api

import com.example.nickelfoxassignment.BuildConfig
import com.example.nickelfoxassignment.imageuploadapp.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApi {

    @Multipart
    @Headers("Authorization: Client-ID ${BuildConfig.CLIENT_ID}")
    @POST("/3/upload")
    suspend fun uploadFile(
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody? = null
    ): Response<UploadResponse>
}