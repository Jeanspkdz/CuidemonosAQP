package com.jean.cuidemonosaqp.shared.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

fun uriToPart(name: String, uri: Uri, contentResolver: ContentResolver): MultipartBody.Part? {
    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

        // Obtener el nombre real del archivo
        val fileName = getFileName(uri, contentResolver) ?: "image.jpg"

        Log.d("URI_TO_PART", "Name: $name, MimeType: $mimeType, FileName: $fileName")

        val bytes = inputStream?.readBytes()
        inputStream?.close()

        if (bytes != null) {
            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData(name, fileName, requestBody)
        } else {
            Log.e("URI_TO_PART", "No se pudieron leer los bytes del archivo")
            null
        }
    } catch (e: Exception) {
        Log.e("URI_TO_PART", "Error convertiendo URI a Part: ${e.message}", e)
        null
    }
}

private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
    var fileName: String? = null

    // Intentar obtener el nombre del archivo desde el content resolver
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
                fileName = it.getString(nameIndex)
            }
        }
    }

    // Si no se pudo obtener el nombre, usar el último segmento del path
    if (fileName == null) {
        fileName = uri.lastPathSegment
    }

    // Si aún no hay nombre, usar un nombre por defecto
    if (fileName == null) {
        fileName = "image_${System.currentTimeMillis()}.jpg"
    }

    return fileName
}

// Función auxiliar para convertir String a RequestBody
fun String.toPlainRequestBody(): okhttp3.RequestBody {
    return this.toRequestBody("text/plain".toMediaTypeOrNull())
}