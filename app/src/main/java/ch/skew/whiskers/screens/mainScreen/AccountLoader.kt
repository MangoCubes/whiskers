package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch.skew.whiskers.data.WhiskersSettings
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyClient
import kotlinx.coroutines.flow.first

@Composable
fun AccountLoader(
    accounts: List<AccountData>,
    addAccount: () -> Unit
) {
    val currentClient = remember { mutableStateOf<MisskeyClient?>(null) }
    val error = remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val filtered = accounts.filter {
            it.accessToken !== null
        }
        if(filtered.isEmpty()) {
            addAccount()
            return@LaunchedEffect
        }
        val accountData = WhiskersSettings(context).getLastAccount.first()?.let { id ->
            filtered.find { it.id == id }
        } ?: filtered[0]
        val client = MisskeyClient.from(accountData)
        if(client !== null) {
            currentClient.value = client
        } else {
            error.value = true
        }
    }
    currentClient.value?.let { Home(it) }
}