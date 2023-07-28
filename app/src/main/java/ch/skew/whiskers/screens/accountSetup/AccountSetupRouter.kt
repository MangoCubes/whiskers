package ch.skew.whiskers.screens.accountSetup

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import ch.skew.whiskers.Pages
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                {
                    val encoded = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(Pages.AccountSetup.Login.route + "/${encoded}")
                }
            )
        }
        composable(route = Pages.AccountSetup.Login.route + "/{instanceUrl}") {
            Login(
                URLDecoder.decode(
                    it.arguments?.getString("instanceUrl"),
                    StandardCharsets.UTF_8.toString()
                )
            ) { navController.popBackStack() }
        }
        composable(
            route = Pages.AccountSetup.Verify.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "whiskers://verify" }
            )
        ) {
            Verify()
        }
    }
}