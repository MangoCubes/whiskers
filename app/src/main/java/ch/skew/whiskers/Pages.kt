package ch.skew.whiskers

sealed class Pages {
    enum class Main(val route: String) {
        Home("main_home"),
        Verify("main_verify")
    }
    enum class AccountSetup(val route: String) {
        Welcome("account_setup_welcome"),
        SelectInstance("account_setup_select_instance"),
        Login("account_setup_login")
    }

    enum class Graphs(val route: String) {
        Main("main"),
        AccountSetup("account_setup")
    }
}