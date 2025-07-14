package com.jean.cuidemonosaqp.shared.preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class Session(
    val token: String? = null,
    val id: String? = null,
)

const val DATA_STORE_FILE_NAME = "user_preferences.json"

object UserPreferencesSerializer : Serializer<Session> {
    override val defaultValue: Session
        get() = Session()

    override suspend fun readFrom(input: InputStream): Session {
        try {
            return Json.decodeFromString(
                deserializer = Session.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserPrefs", serialization)
        }
    }

    override suspend fun writeTo(t: Session, output: OutputStream) {
        output.write(
            Json.encodeToString(Session.serializer(), t)
                .encodeToByteArray()
        )
    }

}