package com.example.nickelfoxassignment.imageuploadapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.Settings
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

@AndroidEntryPoint
class ImageUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageUploadBinding
    private val imageViewModel by viewModels<ImageUploadViewModel>()
    private var imageUri: Uri? = null

    private val selectImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.ivSelectedImage.setImageURI(uri)
                binding.tvResultLink.text = EMPTY_STRING
                imageUri = uri
            }
        }

    private val clickImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                binding.ivSelectedImage.setImageURI(imageUri)
                addPicToGallery()
                binding.tvResultLink.text = EMPTY_STRING
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

    private val requestMultipleCameraPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.any { !it.value }) {
                this.shortToast(resources.getString(R.string.permission_denied))
            } else {
                takeImage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_green)
        setupListeners()
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
                showImageOptionsDialog()
            }
            btnUploadImage.setOnClickListener {
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

    /**
     * Observe the result of the image upload api
     */
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

    /**
     * alert dialog to give user a option to select between camera and gallery for uploading a image
     */
    private fun showImageOptionsDialog() {
        val minSdk29OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        MaterialAlertDialogBuilder(this@ImageUploadActivity)
            .setTitle(resources.getString(R.string.choose_option))
            .setMessage(resources.getString(R.string.choose_message))
            .setNegativeButton(CAMERA) { dialog, _ ->
                when {
                    checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || minSdk29OrAbove) ->
                        takeImage()
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showPermissionDialog(
                        resources.getString(R.string.enable_camera_permission)
                    )
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showPermissionDialog(
                        resources.getString(R.string.enable_media_permission)
                    )
                    else -> if (minSdk29OrAbove) requestMultipleCameraPermissions.launch(
                        arrayOf(Manifest.permission.CAMERA)
                    )
                    else requestMultipleCameraPermissions.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    )
                }
                dialog.dismiss()
            }
            .setPositiveButton(GALLERY) { dialog, _ ->
                if (!minSdk29OrAbove) {
                    when {
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->
                            chooseImageGallery()
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ->
                            showPermissionDialog(resources.getString(R.string.enable_media_permission))
                        else -> requestReadPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else chooseImageGallery()
                dialog.dismiss()
            }.show()
    }

    /**
     * alert dialog to tell user to provide a specific permission if user denied it previously
     */
    private fun showPermissionDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.enable_permission))
            .setMessage(message)
            .setPositiveButton(APP_SETTINGS) { dialog, _ ->
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(PACKAGE, packageName, null)
                    data = uri
                    dialog.dismiss()
                    startActivity(this)
                }
            }
            .setNegativeButton(NOT_NOW) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * to launch an result API for selecting an image from gallery
     */
    private fun chooseImageGallery() {
        selectImage.launch(GALLERY_MIME_TYPE)
    }

    /**
     * to launch an result API which opens a camera to click a picture
     */
    private fun takeImage() {
        imageUri = getTmpFileUri()
        clickImage.launch(imageUri)
    }

    /**
     * provide a temp uri to takeImage() function to store the data of image clicked by user
     */
    private fun getTmpFileUri(): Uri {
        val file = File(applicationContext.filesDir, "JPEG_${System.currentTimeMillis()}_.jpg")
        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }

    /**
     * function to convert a uri into bitmap and give it to addPicToGallery() function
     */
    private fun getBitmapFromUri(uri: Uri?): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            contentResolver.openFileDescriptor(uri!!, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }

    /**
     * function to add an image to external storage
     */
    private fun addPicToGallery() {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val bitMap = getBitmapFromUri(imageUri)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_${System.currentTimeMillis()}_.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, ADD_GALLERY_MIME_TYPE)
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

    /**
     * to show and hide the buttons and progress bar
     */
    private fun viewVisibility(btnVisibility: Boolean, progressBarVisibility: Boolean) {
        binding.piProgressIndicator.isVisible = progressBarVisibility
        binding.btnUploadImage.isVisible = btnVisibility
        binding.btnSelectImage.isVisible = btnVisibility
    }

    companion object {
        const val CAMERA = "Camera"
        const val GALLERY = "Gallery"
        const val APP_SETTINGS = "App Settings"
        const val NOT_NOW = "Not Now"
        const val PACKAGE = "package"
        const val GALLERY_MIME_TYPE = "image/*"
        const val ADD_GALLERY_MIME_TYPE = "image/jpeg"
        const val EMPTY_STRING = ""
    }
}