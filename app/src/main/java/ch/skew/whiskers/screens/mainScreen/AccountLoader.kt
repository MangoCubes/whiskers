package ch.skew.whiskers.screens.mainScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch.skew.whiskers.data.WhiskersDB
import ch.skew.whiskers.data.WhiskersPersistent
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.data.settings.Settings
import ch.skew.whiskers.misskey.MisskeyClient
import kotlinx.coroutines.flow.first

@Composable
fun AccountLoader(
    accounts: List<AccountData>,
    addAccount: () -> Unit,
    manageAccounts: () -> Unit
) {
    val currentClient = remember { mutableStateOf<Pair<MisskeyClient, Settings>?>(null) }
    val context = LocalContext.current

    suspend fun loadAccount(accountId: Int?) {
        val accountData = accountId?.let { id ->
            accounts.find { it.id == id }
        } ?: accounts[0]
        val client = MisskeyClient.from(accountData)
        val settings = accountData.settings?.let {
            WhiskersDB.getInstance(context).settingsDao.getSettings(it)
        } ?: Settings(-1)
        currentClient.value = Pair(client, settings)
    }

    LaunchedEffect(Unit) {
        if(accounts.isEmpty()) {
            addAccount()
            return@LaunchedEffect
        }
        loadAccount(WhiskersPersistent(context).getLastAccount.first())
    }

    currentClient.value?.let { client ->
        Home(accounts, client.first, addAccount, { id ->
            val account = accounts.find {
                it.id == id
            }
            WhiskersPersistent(context).saveLastAccount(id)
            if(account == null) return@Home false
            else {
                loadAccount(id)
                return@Home true
            }
        }, manageAccounts, client.second)
    }
}