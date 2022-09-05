package com.example.nickelfoxassignment.imageuploadapp.repo

import android.content.Context
import android.net.Uri
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.imageuploadapp.api.ImgurApi
import com.example.nickelfoxassignment.imageuploadapp.model.Data
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val imgurApi: ImgurApi,
    private val context: Context
) {
    suspend fun uploadFile(uri: Uri, title: String?): Result<Data> {
        return try {
            val file = copyStreamToFile(uri)

            val filePart =
                MultipartBody.Part.createFormData("image", file.name, file.asRequestBody())

            val response = imgurApi.uploadFile(
                filePart,
                name = title?.toRequestBody() ?: file.name.toRequestBody()
            )

            if (response.isSuccessful) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(context.resources.getString(R.string.exception)))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun copyStreamToFile(uri: Uri): File {
        val outputFile = File.createTempFile("temp", null)

        context.contentResolver.openInputStream(uri)?.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
        return outputFile
    }
}