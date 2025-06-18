package com.jean.cuidemonosaqp.shared.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(isoDate: String): String {
    return try {
        val date = OffsetDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        "Miembro desde el ${date.format(formatter)}"
    } catch (e: Exception) {
        isoDate // fallback si falla el parseo
    }
}