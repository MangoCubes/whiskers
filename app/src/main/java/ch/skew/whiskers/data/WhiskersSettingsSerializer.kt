package ch.skew.whiskers.data

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object WhiskersSettingsSerializer: Serializer<WhiskersSettings> {
    override val defaultValue: WhiskersSettings
        get() = WhiskersSettings()

    override suspend fun readFrom(input: InputStream): WhiskersSettings {
        return try {
            Json.decodeFromString(
                deserializer = WhiskersSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: WhiskersSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = WhiskersSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}