package ch.skew.whiskers.screens.mainScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.classes.DataQueryStatus
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.misskey.MisskeyClient
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.UserDetailed
import ch.skew.whiskers.misskey.error.api.AuthenticationError
import ch.skew.whiskers.misskey.error.api.ClientError
import ch.skew.whiskers.misskey.error.api.ForbiddenError
import ch.skew.whiskers.misskey.error.api.InternalServerError
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

sealed class UserQuery {
    object Querying: UserQuery()
    data class Error(val error: Throwable): UserQuery()
    data class Success(val userData: UserDetailed): UserQuery()
}

@Composable
@Preview
fun HomePreview() {
    Home(listOf(), MisskeyClient("", MisskeyAPI(""), ""), {}, {_, _ -> return@Home true })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    accountData: List<AccountData>,
    account: MisskeyClient,
    addAccount: () -> Unit,
    selectAccount: (String, String) -> Boolean
) {
    val userQuery = remember { mutableStateOf<UserQuery>(UserQuery.Querying) }
    val notesQuery = remember { mutableStateOf<DataQueryStatus<List<Note>>>(DataQueryStatus.Querying(false)) }
    val scope = rememberCoroutineScope()

    suspend fun loadUserData() {
        userQuery.value = UserQuery.Querying
        account.getDetailedUserData().fold(
            { userQuery.value = UserQuery.Success(it) },
            { userQuery.value = UserQuery.Error(it) }
        )
    }

    suspend fun loadNotes() {
        account.getNotesTimeline().fold(
            {
                notesQuery.value = DataQueryStatus.Success(it)
            },
            {
                notesQuery.value = DataQueryStatus.Error(it)
            }
        )
    }

    suspend fun reloadNotes() {
        notesQuery.value = DataQueryStatus.Querying(true)
        loadNotes()
    }

    LaunchedEffect(account) {
        launch {
            userQuery.value = UserQuery.Querying
            loadUserData()
        }
        launch {
            notesQuery.value = DataQueryStatus.Querying(false)
            loadNotes()
        }
    }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerContent = {
            Drawer(
                account,
                accountData,
                addAccount
            ) { name, host ->
                scope.launch {
                    drawerState.close()
                    if(!selectAccount(name, host)) {
                        Toast.makeText(
                            context,
                            "Account not found; defaulting to the first account.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        },
        gesturesEnabled = true,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    {
                        Row(
                            modifier = Modifier
                                .clickable { }
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            userQuery.value.let {
                                when(it){
                                    is UserQuery.Success -> {
                                        Image(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            painter = rememberAsyncImagePainter(it.userData.avatarUrl),
                                            contentDescription = account.username
                                        )
                                    }
                                    is UserQuery.Querying -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                    is UserQuery.Error -> {
                                        IconButton(
                                            modifier = Modifier.size(40.dp),
                                            onClick = {
                                                scope.launch {
                                                    loadUserData()
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Reload")
                                        }
                                    }
                                }
                            }
                            Text("Home")
                        }
                    },
                    navigationIcon = {
                        IconButton({
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, "Sidebar")
                        }
                    },
                    actions = {
                        IconButton({}) {
                            Icon(Icons.Filled.MoreVert, "More")
                        }
                    }
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier.padding(padding)
                    .padding(16.dp)
            ) {
                notesQuery.value.let {
                    when(it) {
                        is DataQueryStatus.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    when(it.error) {
                                        is AuthenticationError -> {
                                            Text("Authentication failed.")
                                        }
                                        is ClientError -> {
                                            Text("Invalid request.")
                                        }
                                        is ForbiddenError -> {
                                            Text("Invalid authentication details.")
                                        }
                                        is InternalServerError -> {
                                            Text("Cannot fetch due to server error.")
                                        }
                                        else -> {
                                            Text("Unknown error.")
                                        }
                                    }
                                    Button(
                                        onClick = { scope.launch { reloadNotes() } }
                                    ) {
                                        Text("Reload")
                                    }
                                }
                            }
                        }
                        is DataQueryStatus.Querying -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is DataQueryStatus.Success -> {

                        }
                    }
                }
            }
        }
    }
}