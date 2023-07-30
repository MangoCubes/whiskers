package ch.skew.whiskers.misskey

import android.net.Uri
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.data.api.AccountIResData


class MisskeyClient(
    val api: MisskeyAPI
) {
    suspend fun getDetailedUserData(): Result<AccountIResData> {
        return this.api.accountI()
    }
    companion object {
        fun from(data: AccountData): MisskeyClient? {
            return if(data.url === null || data.token === null) null
            else MisskeyClient(
                MisskeyAPI(
                    data.token,
                    Uri.parse(data.url)
                )
            )
        }
    }
}