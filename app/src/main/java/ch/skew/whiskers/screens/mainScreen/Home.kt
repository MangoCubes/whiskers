package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ch.skew.whiskers.data.accounts.AccountData

@Composable
fun Home(
    accounts: List<AccountData>,
    addAccount: () -> Unit
) {
    LaunchedEffect(Unit) {
        if(accounts.isEmpty()) {
            addAccount()
            return@LaunchedEffect
        }
    }
}