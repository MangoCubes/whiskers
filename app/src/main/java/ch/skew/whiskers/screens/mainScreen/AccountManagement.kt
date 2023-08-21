package ch.skew.whiskers.screens.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.data.accounts.AccountData
import androidx.compose.material3.ListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountManagement(
    accounts: List<AccountData>,
    goBack: () -> Unit,
    addAccount: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                { Text("Manage Accounts") },
                navigationIcon = {
                    IconButton(goBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Go back") }
                },
                actions = {
                    IconButton(addAccount) { Icon(Icons.Filled.Add, contentDescription = "Add account") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
        ) {
            accounts.map {
                ListItem(
                    headlineContent = { Text(it.username) },
                    supportingContent = { Text(it.host) },
                )
            }
        }
    }
}