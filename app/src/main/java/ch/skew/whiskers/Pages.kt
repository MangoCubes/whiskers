package ch.skew.whiskers

sealed class Pages {
    enum class Main(val route: String) {

    }
    enum class Account(val route: String) {
        Welcome("account_welcome"),
        SelectInstance("account_select_instance")
    }
    enum class Loading(val route: String) {
        Accounts("loading_accounts")
    }
}