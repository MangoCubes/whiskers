package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch.skew.whiskers.data.WhiskersPersistent
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyClient
import ch.skew.whiskers.settings
import kotlinx.coroutines.flow.first

@Composable
fun AccountLoader(
    accounts: List<AccountData>,
    addAccount: () -> Unit,
    manageAccounts: () -> Unit
) {
    val currentClient = remember { mutableStateOf<MisskeyClient?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if(accounts.isEmpty()) {
            addAccount()
            return@LaunchedEffect
        }
        val accountData = accounts[0]
        /**
         * WhiskersPersistent(context).getLastAccount.first()?.let { name ->
         *             accounts.find { it.username == name }
         *         } ?:
         */
        val client = MisskeyClient.from(accountData)
        currentClient.value = client
    }
    currentClient.value?.let { client ->
        Home(accounts, client, addAccount, { name, host ->
            val account = accounts.find {
                it.username == name && it.host == host
            }
            if(account == null) return@Home false
            else {
                currentClient.value = MisskeyClient.from(account)
                return@Home true
            }
        }, manageAccounts)
    }
}