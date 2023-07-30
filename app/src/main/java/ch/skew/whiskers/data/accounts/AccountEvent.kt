package ch.skew.whiskers.data.accounts

sealed interface AccountEvent {
    data class ActivateAccount(val id: Int, val url: String, val appSecret: String, val token: String): AccountEvent
    data class DeleteAccount(val id: Int): AccountEvent
    data class SaveAccessToken(val id: Int, val accessToken: String): AccountEvent
}

sealed interface AccountEventAsync {
    object InsertAccountAsync: AccountEventAsync
}