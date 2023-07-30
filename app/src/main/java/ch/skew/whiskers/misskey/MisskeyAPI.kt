package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIReqData
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData
import ch.skew.whiskers.misskey.data.api.PingReqData
import ch.skew.whiskers.misskey.data.api.PingResData
import ch.skew.whiskers.misskey.error.api.AuthenticationError
import ch.skew.whiskers.misskey.error.api.ClientError
import ch.skew.whiskers.misskey.error.api.ForbiddenError
import ch.skew.whiskers.misskey.error.api.ImAiError
import ch.skew.whiskers.misskey.error.api.InternalServerError
import ch.skew.whiskers.misskey.error.api.UnknownResponseError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MisskeyAPI(
    private val accessToken: String,
    private val instance: Uri,
) {
    suspend fun notesTimeline(limit: NotesTimelineReqData = NotesTimelineReqData()): Result<List<Note>> {
        return this.queryWithAuth(listOf("notes", "timeline"), limit)
    }
    suspend fun accountI(): Result<AccountIResData> {
        return this.queryWithAuth(listOf("i"), AccountIReqData)
    }

    private suspend inline fun <reified REQ, reified RES> queryWithAuth(endpoint: List<String>, body: REQ): Result<RES> {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return try {
            val res = client.post(instance.toString()) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                url {
                    appendPathSegments("api")
                    appendPathSegments(endpoint)
                }
                contentType(ContentType.parse("application/json"))
                setBody(body)
            }
            when (res.status.value) {
                200 -> Result.success(res.body())
                403 -> Result.failure(ForbiddenError(res.body()))
                418 -> Result.failure(ImAiError(res.body()))
                400 -> Result.failure(ClientError(res.body()))
                401 -> Result.failure(AuthenticationError(res.body()))
                500 -> Result.failure(InternalServerError(res.body()))
                else -> Result.failure(UnknownResponseError())
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
    companion object {
        suspend inline fun <reified REQ, reified RES> queryWithoutAuth(instance: String, endpoint: List<String>, body: REQ): Result<RES> {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
            }
            return try {
                val res = client.post(instance) {
                    url {
                        appendPathSegments("api")
                        appendPathSegments(endpoint)
                    }
                    contentType(ContentType.parse("application/json"))
                    setBody(body)
                }
                when (res.status.value) {
                    200 -> Result.success(res.body())
                    403 -> Result.failure(ForbiddenError(res.body()))
                    418 -> Result.failure(ImAiError(res.body()))
                    400 -> Result.failure(ClientError(res.body()))
                    401 -> Result.failure(AuthenticationError(res.body()))
                    500 -> Result.failure(InternalServerError(res.body()))
                    else -> Result.failure(UnknownResponseError())
                }
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        suspend fun ping(instance: String): Boolean {
            val response = queryWithoutAuth<_, PingResData>(instance, listOf("app", "ping"), PingReqData)
            return response.isSuccess
        }
    }
}