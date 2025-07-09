package com.jean.cuidemonosaqp.modules.safeZone.test.runner

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.jean.cuidemonosaqp.modules.safeZone.domain.usecase.CreateSafeZoneUseCase
import com.jean.cuidemonosaqp.shared.utils.toPlainRequestBody
import com.jean.cuidemonosaqp.shared.utils.uriToPart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SafeZoneTestRunner @Inject constructor(
    private val createSafeZoneUseCase: CreateSafeZoneUseCase
) {
    fun createTestZone(contentResolver: ContentResolver, imageUri: Uri?) {
        val fields = mutableMapOf<String, RequestBody>()

        // Obligatorios
        fields["name"] = "Zona nueva".toPlainRequestBody()
        fields["justification"] = "Lugar con buena vigilancia".toPlainRequestBody()
        fields["assumes_responsibility"] = "true".toPlainRequestBody()
        fields["latitude"] = "-15.41".toPlainRequestBody()
        fields["longitude"] = "-71.52".toPlainRequestBody()
        fields["status_id"] = "4".toPlainRequestBody()
        fields["user_ids"] = "1,2,3".toPlainRequestBody()
        fields["rating"] = "4.5".toPlainRequestBody()

        // Opcionales
        fields["description"] = "Descripción de test".toPlainRequestBody()
        fields["category"] = "Plaza".toPlainRequestBody()

        val imagePart: MultipartBody.Part? = imageUri?.let {
            uriToPart("photo_url", it, contentResolver)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = createSafeZoneUseCase(fields, imagePart)
                Log.d("SafeZoneRunner", "✅ Zona creada: ${response.name}")
            } catch (e: Exception) {
                Log.e("SafeZoneRunner", "❌ Error: ${e.message}")
            }
        }
    }
}
