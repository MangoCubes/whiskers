package ch.skew.whiskers.data.accounts

sealed interface AccountEvent {
    data class ActivateAccount(val url: String, val token: String): AccountEvent
    data class DeleteAccount(val id: Int): AccountEvent
}

sealed interface AccountEventAsync {
    object InsertAccountAsync: AccountEventAsync
}