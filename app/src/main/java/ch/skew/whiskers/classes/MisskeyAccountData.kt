package ch.skew.whiskers.classes

import ch.skew.whiskers.data.accounts.AccountData

data class MisskeyAccountData (
    val id: Int,
    val url: String,
    val accessToken: String,
    val username: String,
) {
    companion object {
        fun from(data: AccountData): MisskeyAccountData? {
            return if(data.url === null || data.accessToken === null || data.username === null) null
            else MisskeyAccountData(data.id, data.url, data.accessToken, data.username)
        }
    }
}