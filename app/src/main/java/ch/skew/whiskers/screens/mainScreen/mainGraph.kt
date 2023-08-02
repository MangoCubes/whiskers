package ch.skew.whiskers.screens.mainScreen

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ch.skew.whiskers.Pages
import ch.skew.whiskers.data.accounts.AccountData


fun NavGraphBuilder.main(
    nav: NavController,
    accounts: List<AccountData>,
    addAccount: () -> Unit,
    saveAccount: (String, String, String, String) -> Unit,
) {
    composable(route = Pages.Main.Home.route) {
        AccountLoader(accounts, addAccount)
    }
    composable(
        route = Pages.Main.Verify.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "whiskers://verify/{id}"
                action = Intent.ACTION_VIEW
            }
        ),
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) {
        val id = it.arguments?.getInt("id")
        Verify(
            id,
            saveAccount,
            addAccount
        ) {
            nav.navigate(Pages.Main.Home.route) {
                popUpTo(0)
            }
        }
    }
}