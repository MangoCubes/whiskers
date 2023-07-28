package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import ch.skew.whiskers.Pages
import ch.skew.whiskers.data.accounts.AccountData

@Composable
fun Router(
    accounts: List<AccountData>
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Pages.Main.Home.route
    ) {
        composable(route = Pages.Main.Home.route) {

        }
        composable(
            route = Pages.Main.Verify.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "whiskers://verify" }
            )
        ) {
            Verify()
        }
    }
}