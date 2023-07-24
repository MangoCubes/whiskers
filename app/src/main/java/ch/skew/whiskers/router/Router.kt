package ch.skew.whiskers.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ch.skew.whiskers.Pages

@Composable
fun Router() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Pages.Loading.Accounts.route
    ) {

    }
}