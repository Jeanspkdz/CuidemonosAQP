package com.jean.cuidemonosaqp.shared.utils
// File: shared/utils/FormUtils.kt

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Extensión para convertir un String a un RequestBody plano.
 * Útil para solicitudes multipart/form-data.
 */
fun String.toPlainRequestBody(): RequestBody =
    toRequestBody("text/plain".toMediaTypeOrNull())