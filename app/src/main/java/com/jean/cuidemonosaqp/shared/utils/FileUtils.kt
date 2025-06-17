package com.jean.cuidemonosaqp.shared.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun uriToPart(name: String, uri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
    return try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val file = File.createTempFile("upload", ".jpg")
        file.outputStream().use { inputStream.copyTo(it) }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(name, file.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
