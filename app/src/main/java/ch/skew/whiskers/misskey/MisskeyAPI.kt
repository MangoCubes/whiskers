package ch.skew.whiskers.misskey

import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.api.AccountIReqData
import ch.skew.whiskers.misskey.data.api.AccountIResData
import ch.skew.whiskers.misskey.data.api.CreateReactionReqData
import ch.skew.whiskers.misskey.data.api.CreateReactionResData
import ch.skew.whiskers.misskey.data.api.EmojisReqData
import ch.skew.whiskers.misskey.data.api.EmojisResData
import ch.skew.whiskers.misskey.data.api.NotesTimelineReqData
import ch.skew.whiskers.misskey.data.api.PingReqData
import ch.skew.whiskers.misskey.data.api.PingResData
import ch.skew.whiskers.misskey.error.api.AuthenticationError
import ch.skew.whiskers.misskey.error.api.ClientError
import ch.skew.whiskers.misskey.error.api.ForbiddenError
import ch.skew.whiskers.misskey.error.api.ImAiError
import ch.skew.whiskers.misskey.error.api.InternalServerError
import ch.skew.whiskers.misskey.error.api.NotFoundError
import ch.skew.whiskers.misskey.error.api.UnknownResponseError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.set
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MisskeyAPI(
    private val instance: String,
) {
    suspend fun createReaction(reaction: CreateReactionReqData): Result<CreateReactionResData>{
        return this.queryWithAuth(listOf("notes", "reactions", "create"), reaction)
    }
    suspend fun notesTimeline(limit: NotesTimelineReqData): Result<List<Note>> {
        return this.queryWithAuth(listOf("notes", "timeline"), limit)
    }
    suspend fun accountI(token: String): Result<AccountIResData> {
        return this.queryWithAuth(listOf("i"), AccountIReqData(token))
    }

    suspend fun metaEmojis(): Result<EmojisResData> {
        return queryWithoutAuth(this.instance, listOf("emojis"), EmojisReqData)
    }

    private suspend inline fun <reified REQ, reified RES> queryWithAuth(endpoint: List<String>, body: REQ): Result<RES> {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return try {
            val res = client.post {
                url {
                    set(scheme = "https", host = instance)
                    appendPathSegments("api")
                    appendPathSegments(endpoint)
                }
                contentType(ContentType.parse("application/json"))
                setBody(body)
            }
            println(res.body<String>())
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
                val res = client.post {
                    url {
                        set(scheme = "https", host = instance)
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
                    // This can exist for emoji endpoint
                    404 -> Result.failure(NotFoundError())
                    else -> Result.failure(UnknownResponseError())
                }
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
        suspend fun ping(instance: String): Boolean {
            val response = queryWithoutAuth<_, PingResData>(instance, listOf("ping"), PingReqData)
            return response.isSuccess
        }
    }
}