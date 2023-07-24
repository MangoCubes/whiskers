package ch.skew.whiskers.screens.addAccount

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.skew.whiskers.Pages

@Composable
fun AccountSetup(
    firstTimeSetup: Boolean
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (firstTimeSetup) Pages.Account.Welcome.route else Pages.Account.SelectInstance.route
    ) {
        composable(route = Pages.Account.Welcome.route) {

        }
    }
}