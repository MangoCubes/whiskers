package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch.skew.whiskers.classes.MisskeyAccountData
import ch.skew.whiskers.data.WhiskersSettings
import ch.skew.whiskers.misskey.MisskeyClient
import kotlinx.coroutines.flow.first

@Composable
fun AccountLoader(
    accounts: List<MisskeyAccountData>,
    addAccount: () -> Unit
) {
    val currentClient = remember { mutableStateOf<MisskeyClient?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if(accounts.isEmpty()) {
            addAccount()
            return@LaunchedEffect
        }
        val accountData = WhiskersSettings(context).getLastAccount.first()?.let { id ->
            accounts.find { it.id == id }
        } ?: accounts[0]
        val client = MisskeyClient.from(accountData)
        currentClient.value = client
    }
    currentClient.value?.let { Home(accounts, it) }
}