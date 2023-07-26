package ch.skew.whiskers.misskey

import ch.skew.whiskers.misskey.data.AppCreateReq
import ch.skew.whiskers.misskey.data.AppCreateRes
import ch.skew.whiskers.misskey.data.AuthError
import com.google.gson.Gson
import io.ktor.client.call.body

class MisskeyLoginClient(
    val appSecret: String
) {
    companion object {
        suspend fun create(instance: String): Result<MisskeyLoginClient> {
            val body = AppCreateReq("Whiskers", "Authorisation for Whiskers app", listOf())
            MisskeyAPI.queryWithoutAuth(instance, listOf("app", "create"), Gson().toJson(body))
                .fold(
                    {
                        val res = it.body<AppCreateRes>()
                        return if (res.secret !== null) {
                            Result.success(MisskeyLoginClient(res.secret))
                        } else Result.failure(AuthError())
                    }, {
                        return Result.failure(it)
                    }
                )
        }
    }
}