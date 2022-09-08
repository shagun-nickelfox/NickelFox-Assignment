package com.example.nickelfoxassignment.imageuploadapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.nickelfoxassignment.BuildConfig
import com.example.nickelfoxassignment.R
import com.example.nickelfoxassignment.databinding.ActivityImageUploadBinding
import com.example.nickelfoxassignment.imageuploadapp.viewmodel.ImageUploadViewModel
import com.example.nickelfoxassignment.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ImageUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageUploadBinding
    private val imageViewModel by viewModels<ImageUploadViewModel>()
    private var imageUri: Uri? = null
    private var latestTmpUri: Uri? = null

    private val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.ivSelectedImage.setImageURI(uri)
                binding.tvResultLink.text = ""
                imageUri = uri
            }
        }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                binding.ivSelectedImage.setImageURI(latestTmpUri)
                imageUri = latestTmpUri
                galleryAddPic()
                binding.tvResultLink.text = ""
            }
        }

    private val requestReadPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                chooseImageGallery()
            } else {
                this.shortToast(resources.getString(R.string.permission_denied))
            }
        }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.any { permission -> !permission.value }) {
                this.shortToast(resources.getString(R.string.permission_denied))
            } else {
                takeImage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColour()
        setupListeners()
    }

    private fun changeStatusBarColour() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green)
    }

    private fun setupListeners() {
        binding.apply {
            piProgressIndicator.setIndicatorColor(
                ContextCompat.getColor(
                    this@ImageUploadActivity,
                    R.color.green
                )
            )
            btnSelectImage.setOnClickListener {
                showImageSelectOptions(true)
            }
            btnTakeImageButton.setOnClickListener {
                requestClickImagePermissions()
            }
            btnSelectImageButton.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestReadPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    chooseImageGallery()
                }
            }
            btnUploadImage.setOnClickListener {
                showImageSelectOptions(false)
                if (imageUri != null) {
                    viewVisibility(btnVisibility = false, progressBarVisibility = true)
                    imageViewModel.selectedImageUri(imageUri!!)
                    setupObservers()
                } else {
                    this@ImageUploadActivity.shortToast(resources.getString(R.string.choose_image))
                }
            }
        }
    }

    private fun requestClickImagePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                requestMultiplePermissions.launch(
                    arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.CAMERA)
                )
            } else if (checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION))
            } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.CAMERA))
            } else {
                takeImage()
            }
        } else {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestMultiplePermissions.launch(arrayOf(Manifest.permission.CAMERA))
            } else {
                takeImage()
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            imageViewModel.uploadImage().onSuccess { data ->
                viewVisibility(btnVisibility = true, progressBarVisibility = false)
                binding.tvResultLink.text = data.link
                this@ImageUploadActivity.shortToast(resources.getString(R.string.image_uploaded))
            }
            imageViewModel.uploadImage().onFailure {
                viewVisibility(btnVisibility = true, progressBarVisibility = false)
                this@ImageUploadActivity.shortToast(resources.getString(R.string.uploading_error))
            }
        }
    }

    private fun showImageSelectOptions(visible: Boolean) {
        binding.btnSelectImageButton.isVisible = visible
        binding.btnTakeImageButton.isVisible = visible
    }

    private fun chooseImageGallery() {
        selectImage.launch("image/*")
    }

    private fun takeImage() {
        latestTmpUri = getTmpFileUri()
        takeImageResult.launch(latestTmpUri)
    }

    private fun getTmpFileUri(): Uri {
        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            createImageFile()
        )
    }

    private fun createImageName(): String {
        val timeStamp = SimpleDateFormat.getDateTimeInstance().format(Date())
        return "JPEG_${timeStamp}_"
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            createImageName(),
            ".jpg",
            storageDir
        )
    }

    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            contentResolver.openFileDescriptor(uri!!, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    private fun galleryAddPic() {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val bitMap = getBitmapFromUri(latestTmpUri)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${createImageName()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        try {
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bitMap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream))
                        throw IOException(resources.getString(R.string.saving_error))
                }
            } ?: throw IOException(resources.getString(R.string.media_store_error))
        } catch (e: IOException) {
            this.shortToast(e.message.toString())
        }
    }

    private fun viewVisibility(btnVisibility: Boolean, progressBarVisibility: Boolean) {
        binding.piProgressIndicator.isVisible = progressBarVisibility
        binding.btnUploadImage.isVisible = btnVisibility
        binding.btnSelectImage.isVisible = btnVisibility
    }
}