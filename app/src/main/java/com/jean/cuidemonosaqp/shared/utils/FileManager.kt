package com.jean.cuidemonosaqp.shared.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

object FileManager {

    private const val PENDING_IMAGES_DIR = "pending_images"

    /**
     * Guarda una imagen desde URI a almacenamiento interno y devuelve la ruta
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri, prefix: String = "img"): String? {
        return try {
            val imagesDir = File(context.filesDir, PENDING_IMAGES_DIR)
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            val fileName = "${prefix}_${UUID.randomUUID()}.jpg"
            val imageFile = File(imagesDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(imageFile).use { output ->
                    input.copyTo(output)
                }
            }

            imageFile.absolutePath
        } catch (e: IOException) {
            Log.e("FileManager", "Error guardando imagen: ${e.message}")
            null
        }
    }

    /**
     * Elimina un archivo del almacenamiento interno
     */
    fun deleteFile(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                file.delete()
            } else {
                true // Ya no existe
            }
        } catch (e: Exception) {
            Log.e("FileManager", "Error eliminando archivo: ${e.message}")
            false
        }
    }

    /**
     * Verifica si un archivo existe
     */
    fun fileExists(filePath: String?): Boolean {
        return filePath?.let { File(it).exists() } ?: false
    }

    /**
     * Obtiene el File desde una ruta
     */
    fun getFile(filePath: String): File? {
        return if (fileExists(filePath)) File(filePath) else null
    }

    /**
     * Limpia archivos antiguos (opcional, para mantenimiento)
     */
    fun cleanOldFiles(context: Context, olderThanDays: Int = 7) {
        try {
            val imagesDir = File(context.filesDir, PENDING_IMAGES_DIR)
            if (!imagesDir.exists()) return

            val cutoffTime = System.currentTimeMillis() - (olderThanDays * 24 * 60 * 60 * 1000L)

            imagesDir.listFiles()?.forEach { file ->
                if (file.lastModified() < cutoffTime) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Log.e("FileManager", "Error limpiando archivos antiguos: ${e.message}")
        }
    }
}