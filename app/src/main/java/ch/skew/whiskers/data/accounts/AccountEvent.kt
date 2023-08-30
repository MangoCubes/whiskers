package ch.skew.whiskers.data.accounts

sealed interface AccountEvent {
    data class AddAccount(val host: String, val appSecret: String, val accessToken: String, val username: String): AccountEvent
    data class DeleteAccount(val host: String, val username: String): AccountEvent
    data class DeleteAccountById(val id: Int): AccountEvent
}