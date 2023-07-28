package ch.skew.whiskers.misskey

import ch.skew.whiskers.misskey.data.api.AppCreateReqData
import ch.skew.whiskers.misskey.data.api.AppCreateResData
import ch.skew.whiskers.misskey.data.api.AuthError
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateReqData
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateResData

class MisskeyLoginClient(
    val instance: String,
    val appSecret: String
) {
    suspend fun generate(): Result<AuthSessionGenerateResData> {
        val body = AuthSessionGenerateReqData(this.appSecret)
        MisskeyAPI.queryWithoutAuth<AuthSessionGenerateReqData, AuthSessionGenerateResData>(instance, listOf("auth", "session", "generate"), body)
            .fold(
                {
                    return Result.success(it)
                }, {
                    return Result.failure(it)
                }
            )
    }
    companion object {
        suspend fun create(instance: String, id: Long): Result<MisskeyLoginClient> {
            val body = AppCreateReqData("Whiskers", "Authorisation for Whiskers app", listOf(), "whiskers://verify?id=$id")
            MisskeyAPI.queryWithoutAuth<AppCreateReqData, AppCreateResData>(instance, listOf("app", "create"), body)
                .fold(
                    {
                        return if (it.secret !== null) {
                            Result.success(MisskeyLoginClient(instance, it.secret))
                        } else Result.failure(AuthError())
                    }, {
                        return Result.failure(it)
                    }
                )
        }
    }
}