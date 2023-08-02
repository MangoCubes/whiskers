package ch.skew.whiskers.screens.accountSetup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ch.skew.whiskers.Pages
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.accountSetup(
    navController: NavController,
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
            ),
        ) { navController.popBackStack() }
    }
}