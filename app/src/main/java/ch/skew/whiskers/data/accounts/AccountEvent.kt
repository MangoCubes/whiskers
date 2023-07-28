package ch.skew.whiskers.data.accounts

sealed interface AccountEvent {
    data class InsertAccount(val url: String, val token: String): AccountEvent
    data class DeleteAccount(val id: Int): AccountEvent
}