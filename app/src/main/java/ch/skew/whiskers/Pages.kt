package ch.skew.whiskers

sealed class Pages {
    enum class Main(val route: String) {
        Home("main_home")
    }
    enum class Account(val route: String) {
        Welcome("account_welcome"),
        SelectInstance("account_select_instance")
    }
}