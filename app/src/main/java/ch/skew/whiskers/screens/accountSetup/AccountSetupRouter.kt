package ch.skew.whiskers.screens.accountSetup

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.skew.whiskers.Pages

@Composable
fun AccountSetupRouter(
    firstTimeSetup: Boolean
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (firstTimeSetup) Pages.AccountSetup.Welcome.route else Pages.AccountSetup.SelectInstance.route
    ) {
        composable(route = Pages.AccountSetup.Welcome.route) {
            Welcome { navController.navigate(Pages.AccountSetup.SelectInstance.route) }
        }
        composable(route = Pages.AccountSetup.SelectInstance.route) {
            SelectInstance (
                { navController.popBackStack() },
                { navController.navigate(Pages.AccountSetup.Login.route) }
            )
        }
        composable(route = Pages.AccountSetup.Login.route) {

        }
        composable(route = Pages.AccountSetup.Register.route) {

        }
    }
}