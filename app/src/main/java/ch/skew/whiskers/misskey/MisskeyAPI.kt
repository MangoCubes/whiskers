package ch.skew.whiskers.misskey

import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

class MisskeyAPI(
    private val token: String
) {
    suspend fun queryWithAuth(instance: String, endpoint: List<String>, body: String): Result<HttpResponse> {
        val client = HttpClient()
        return try {
            val res = client.post(instance) {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                url {
                    appendPathSegments(endpoint)
                }
                contentType(ContentType.parse("application/json"))
                setBody(body)
            }
            Result.success(res)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
    companion object {
        suspend fun queryWithoutAuth(instance: String, endpoint: List<String>, body: String): Result<HttpResponse> {
            val client = HttpClient()
            return try {
                val res = client.post(instance) {
                    url {
                        appendPathSegments(endpoint)
                    }
                    contentType(ContentType.parse("application/json"))
                    setBody(body)
                }
                Result.success(res)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        suspend fun ping(instance: String): Boolean {
            val response = queryWithoutAuth(instance, listOf("app", "ping"), "{}").getOrNull()
            return response?.status?.value in 200..299
        }


    }
}