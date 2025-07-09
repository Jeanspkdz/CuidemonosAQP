package com.jean.cuidemonosaqp.modules.safeZone.domain.usecase

import com.jean.cuidemonosaqp.modules.safeZone.data.model.SafeZoneResponse
import com.jean.cuidemonosaqp.modules.safeZone.domain.repository.SafeZoneRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Caso de uso para crear una nueva zona segura.
 */
class CreateSafeZoneUseCase @Inject constructor(
    private val repository: SafeZoneRepository
) {
    suspend operator fun invoke(
        fields: Map<String, RequestBody>,
        photoPart: MultipartBody.Part?
    ): SafeZoneResponse {
        return repository.createSafeZoneWithImage(fields, photoPart)
    }
}