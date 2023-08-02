package ch.skew.whiskers.screens.mainScreen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyLoginClient

enum class QueryState {
    AccountNotFound,
    Querying,
    Success,
    NotAuthorised
}
@Composable
@Preview
fun VerifyPreview(){
    Verify(1, listOf(), {_, _, _ -> }, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify(
    id: Int?,
    accounts: List<AccountData>,
    addAccessToken: (Int, String, String) -> Unit,
    retry: () -> Unit,
    goHome: () -> Unit
) {
    val state = remember { mutableStateOf(QueryState.Querying) }
    val message = remember { mutableStateOf("Verifying account...") }
    LaunchedEffect(Unit) {
        if (id === null) {
            state.value = QueryState.AccountNotFound
            message.value = "Invalid access."
            return@LaunchedEffect
        }
        val account = accounts.find { it.id == id }?.let { MisskeyLoginClient.from(it) }
        if(account === null) {
            state.value = QueryState.AccountNotFound
            message.value = "Account has not been added to the app."
            return@LaunchedEffect
        } else {
            account.userkey().fold(
                {
                    addAccessToken(id, it.accessToken, it.user.username)
                    state.value = QueryState.Success
                    message.value = "Account successfully added! Welcome back ${it.user.name}."
                },
                {
                    state.value = QueryState.NotAuthorised
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
                QueryState.Querying -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(120.dp)
                    )
                }
                QueryState.Success -> {
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
                enabled = state.value !== QueryState.Querying
            ) {
                Text("Go To Home")
            }
            Button(
                retry,
                enabled = state.value !== QueryState.Querying
            ) {
                Text("Add Another Account")
            }
        }
    }
}