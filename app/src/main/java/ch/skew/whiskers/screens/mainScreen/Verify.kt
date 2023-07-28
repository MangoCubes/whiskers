package ch.skew.whiskers.screens.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ch.skew.whiskers.data.accounts.AccountData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Verify(
    id: Int?,
    accounts: List<AccountData>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                { Text("Account Setup") }
            )
        },
    ) { padding ->
        val account = if (id === null) null else accounts.find { it.id == id }
        Column(modifier = Modifier.padding(padding)) {
            if(account === null) {
                Text("Failed to add account.")
            } else {
                Text("Account added! (ID: $id)")
            }
        }
    }
}