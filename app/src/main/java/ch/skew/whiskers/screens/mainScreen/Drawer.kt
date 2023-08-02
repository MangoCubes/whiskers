package ch.skew.whiskers.screens.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.misskey.MisskeyClient

val example = listOf(
    AccountData("example1.com", "", "", "User1"),
    AccountData("example2.com", "", "", "User2"),
    AccountData("example3.com", "", "", "User3"),
    AccountData("example4.com", "", "", "User4"),
)

@Composable
@Preview
fun DrawerPreview() {
    Drawer(account = MisskeyClient(accessToken = "", api = MisskeyAPI(""), username = "User1"), example) {}
}

val itemModifier = Modifier.padding(horizontal = 12.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(
    account: MisskeyClient,
    accountData: List<AccountData>,
    addAccount: () -> Unit
) {
    val expandAccounts = remember { mutableStateOf(false) }
    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("Accounts") },
            selected = false,
            onClick = { expandAccounts.value = !expandAccounts.value },
            icon = {
                if(expandAccounts.value)
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Expand accounts")
                else Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Collapse accounts")
            },
            modifier = itemModifier
        )
        AnimatedVisibility(
            visible = expandAccounts.value,
        ) {
            Column {
                accountData.forEach {
                    NavigationDrawerItem(
                        label = { Text(
                            buildAnnotatedString {
                                append(it.username)
                                withStyle(SpanStyle(color = Color.Gray)) {
                                    append(" (${it.host})")
                                }
                            }
                        ) },
                        selected = it.username == account.username,
                        onClick = {},
                        modifier = itemModifier
                    )
                }
            }
        }
        NavigationDrawerItem(
            label = { Text("Add account") },
            selected = false,
            onClick = addAccount,
            icon = {
                Icon(Icons.Filled.Add, contentDescription = "Add Account")
            },
            modifier = itemModifier
        )
    }
}