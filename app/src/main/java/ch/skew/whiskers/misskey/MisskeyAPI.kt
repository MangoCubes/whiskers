package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIReqData
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData
import ch.skew.whiskers.misskey.data.api.PingReqData
import ch.skew.whiskers.misskey.data.api.PingResData
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

class MisskeyAPI(
    private val appSecret: String,
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
                json()
            }
        }
        return try {
            val res = client.post(instance.toString()) {
                headers {
                    append(HttpHeaders.Authorization, appSecret)
                }
                url {
                    appendPathSegments("api")
                    appendPathSegments(endpoint)
                }
                contentType(ContentType.parse("application/json"))
                setBody(body)
            }
            Result.success(res.body())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
    companion object {
        suspend inline fun <reified REQ, reified RES> queryWithoutAuth(instance: String, endpoint: List<String>, body: REQ): Result<RES> {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json()
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
                Result.success(res.body())
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