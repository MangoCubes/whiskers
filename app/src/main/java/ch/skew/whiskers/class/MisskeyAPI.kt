package ch.skew.whiskers.`class`

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

class MisskeyAPI {
    companion object {
        suspend fun ping(instance: String): Boolean {
            val client = HttpClient()
            return try {
                val response = client.post(instance) {
                    url {
                        appendPathSegments("api", "ping")
                    }
                    contentType(ContentType.parse("application/json"))
                    setBody("{}")
                }
                println(response.body<String>())
                (response.status.value in 200..299)
            } catch (e: Throwable) {
                false
            }
        }
    }
}