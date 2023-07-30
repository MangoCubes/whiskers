package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.misskey.data.api.AppCreateReqData
import ch.skew.whiskers.misskey.data.api.AppCreateResData
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateReqData
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateResData
import ch.skew.whiskers.misskey.error.AppCreationError
import ch.skew.whiskers.misskey.error.SessionGenerationError

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
                    return Result.failure(SessionGenerationError(it))
                }
            )
    }
    companion object {
        suspend fun create(instance: Uri, id: Int): Result<MisskeyLoginClient> {
            val instanceStr = instance.toString()
            val body = AppCreateReqData("Whiskers", "Authorisation for Whiskers app", listOf(), "whiskers://verify/$id")
            MisskeyAPI.queryWithoutAuth<AppCreateReqData, AppCreateResData>(instanceStr, listOf("app", "create"), body)
                .fold(
                    {
                        return if (it.secret !== null) {
                            Result.success(MisskeyLoginClient(instanceStr, it.secret))
                        } else Result.failure(AppCreationError())
                    }, {
                        return Result.failure(AppCreationError(cause = it))
                    }
                )
        }
    }
}