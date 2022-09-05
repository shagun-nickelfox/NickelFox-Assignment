package com.example.nickelfoxassignment.imageuploadapp.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nickelfoxassignment.imageuploadapp.model.Data
import com.example.nickelfoxassignment.imageuploadapp.repo.UploadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(uploadRepository: UploadRepository) : ViewModel() {
    private val imageUri = MutableLiveData<Uri>()
    private val repository = uploadRepository

    fun selectedImageUri(uri: Uri) {
        imageUri.value = uri
    }

    suspend fun uploadImage(): Result<Data> {
        return imageUri.value.let { repository.uploadFile(it!!, null) }
    }
}