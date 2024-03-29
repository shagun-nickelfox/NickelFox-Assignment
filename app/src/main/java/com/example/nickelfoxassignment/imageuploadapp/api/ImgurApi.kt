package com.example.nickelfoxassignment.imageuploadapp.api

import com.example.nickelfoxassignment.imageuploadapp.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApi {

    @Multipart
    @POST("/3/upload")
    suspend fun uploadFile(
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>
}