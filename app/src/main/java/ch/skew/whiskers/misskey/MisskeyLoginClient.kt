package ch.skew.whiskers.misskey

import ch.skew.whiskers.misskey.data.AppCreateReqData
import ch.skew.whiskers.misskey.data.AppCreateResData
import ch.skew.whiskers.misskey.data.AuthError
import ch.skew.whiskers.misskey.data.AuthSessionGenerateReqData
import ch.skew.whiskers.misskey.data.AuthSessionGenerateResData

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
        suspend fun create(instance: String): Result<MisskeyLoginClient> {
            val body = AppCreateReqData("Whiskers", "Authorisation for Whiskers app", listOf(), "whiskers://verify")
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