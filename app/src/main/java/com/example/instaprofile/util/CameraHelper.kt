package com.example.instaprofile.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private val activity: Activity) {
    var imageUrl: Uri? = null
    val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    val REQUEST_CODE = 1

    fun takeCameraPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            val imageFile = createImageFile()
            imageUrl =
                FileProvider.getUriForFile(
                    activity,
                    "com.example.instaprofile.fileprovider",
                    imageFile
                )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun createImageFile(): File {
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${simpleDateFormat.format(Date())}_",
            ".jpg",
            storageDir
        )
    }
}