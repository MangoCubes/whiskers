package ch.skew.whiskers

sealed class Pages {
    enum class Main(val route: String) {
        Home("main_home")
    }
    enum class AccountSetup(val route: String) {
        Welcome("account_setup_welcome"),
        SelectInstance("account_setup_select_instance"),
        Login("account_setup_login"),
        Register("account_setup_register")
    }
}