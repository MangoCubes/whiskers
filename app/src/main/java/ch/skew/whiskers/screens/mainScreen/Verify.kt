package ch.skew.whiskers.screens.mainScreen

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.data.WhiskersPersistent
import ch.skew.whiskers.misskey.MisskeyLoginClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first

enum class QueryStatus {
    AccountNotFound,
    Querying,
    Success,
    NotAuthorised,
    Duplicate,
    SavingFailed
}
@Composable
@Preview
fun VerifyPreview(){
    val scope = rememberCoroutineScope()
    Verify(1, {_, _, _, _ -> scope.async { return@async null } }, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify(
    id: Int?,
    saveAccount: (String, String, String, String) -> Deferred<Throwable?>,
    retry: () -> Unit,
    goHome: () -> Unit
) {
    val state = remember { mutableStateOf(QueryStatus.Querying) }
    val message = remember { mutableStateOf("Verifying account...") }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (id === null) {
            state.value = QueryStatus.AccountNotFound
            message.value = "Invalid access."
            return@LaunchedEffect
        }
        val settings = WhiskersPersistent(context)
        val account = settings.getAuthInProgress.first()
        if(account === null) {
            state.value = QueryStatus.AccountNotFound
            message.value = "Account has not been added to the app."
            return@LaunchedEffect
        } else {
            val loginClient = MisskeyLoginClient(account.host, account.appSecret, account.token)
            loginClient.userkey().fold(
                { auth ->
                    val e = saveAccount(
                        account.host,
                        account.appSecret,
                        auth.accessToken,
                        auth.user.username
                    ).await()
                    if(e === null) {
                        state.value = QueryStatus.Success
                        message.value = "Account successfully added! Welcome back ${auth.user.name}."
                    } else {
                        if(e is SQLiteConstraintException) {
                            state.value = QueryStatus.Duplicate
                            message.value = "Account is already added in this app."
                        } else {
                            state.value = QueryStatus.SavingFailed
                            message.value = "Failed to save account."
                        }
                    }
                },
                {
                    state.value = QueryStatus.NotAuthorised
                    message.value = "Cannot retrieve your account from the server. Please try again."
                }
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                { Text("Account Setup") }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when(state.value) {
                QueryStatus.Querying -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(120.dp)
                    )
                }
                QueryStatus.Success -> {
                    Icon(
                        Icons.Filled.Check,
                        "Success",
                        modifier = Modifier.size(120.dp)
                    )
                }
                else -> {
                    Icon(
                        Icons.Filled.Close,
                        "Failed",
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
            Text(message.value)
            Button(
                goHome,
                enabled = state.value !== QueryStatus.Querying
            ) {
                Text("Go To Home")
            }
            Button(
                retry,
                enabled = state.value !== QueryStatus.Querying
            ) {
                Text("Add Another Account")
            }
        }
    }
}