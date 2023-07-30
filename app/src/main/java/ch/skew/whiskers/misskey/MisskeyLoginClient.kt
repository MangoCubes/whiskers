package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.data.api.AppCreateReqData
import ch.skew.whiskers.misskey.data.api.AppCreateResData
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateReqData
import ch.skew.whiskers.misskey.data.api.AuthSessionGenerateResData
import ch.skew.whiskers.misskey.data.api.AuthSessionUserkeyReqData
import ch.skew.whiskers.misskey.data.api.AuthSessionUserkeyResData
import ch.skew.whiskers.misskey.error.NoSecretError
import ch.skew.whiskers.misskey.error.NoTokenError

class MisskeyLoginClient(
    val instance: String,
    val appSecret: String,
    val token: String? = null
) {
    suspend fun generate(): Result<AuthSessionGenerateResData> {
        val body = AuthSessionGenerateReqData(this.appSecret)
        MisskeyAPI.queryWithoutAuth<_, AuthSessionGenerateResData>(instance, listOf("auth", "session", "generate"), body)
            .fold(
                {
                    return Result.success(it)
                }, {
                    return Result.failure(it)
                }
            )
    }

    suspend fun userkey(): Result<AuthSessionUserkeyResData> {
        if(token == null) return Result.failure(NoTokenError())
        val body = AuthSessionUserkeyReqData(this.appSecret, token)
        MisskeyAPI.queryWithoutAuth<_, AuthSessionUserkeyResData>(instance, listOf("auth", "session", "userkey"), body)
            .fold(
                {
                    return Result.success(it)
                },
                {
                    return Result.failure(it)
                }
            )
    }
    companion object {
        suspend fun create(instance: Uri, id: Int): Result<MisskeyLoginClient> {
            val instanceStr = instance.toString()
            val body = AppCreateReqData("Whiskers", "Authorisation for Whiskers app", listOf(), "whiskers://verify/$id")
            MisskeyAPI.queryWithoutAuth<_, AppCreateResData>(instanceStr, listOf("app", "create"), body)
                .fold(
                    {
                        return if (it.secret !== null) {
                            Result.success(MisskeyLoginClient(instanceStr, it.secret))
                        } else Result.failure(NoSecretError())
                    }, {
                        return Result.failure(it)
                    }
                )
        }
        fun from(account: AccountData): MisskeyLoginClient? {
            return if(account.url !== null && account.appSecret !== null && account.token !== null)
                MisskeyLoginClient(account.url, account.appSecret, account.token)
            else null
        }
    }
}