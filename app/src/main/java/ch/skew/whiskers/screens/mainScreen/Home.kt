package ch.skew.whiskers.screens.mainScreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.classes.MisskeyAccountData
import ch.skew.whiskers.classes.QueryStatus
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.misskey.MisskeyClient
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.UserDetailed
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
    Home(listOf(), MisskeyClient("", MisskeyAPI(Uri.parse("")), ""), {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    accountData: List<MisskeyAccountData>,
    account: MisskeyClient,
    addAccount: () -> Unit
) {
    val userQuery = remember { mutableStateOf<UserQuery>(UserQuery.Querying) }
    val noteQuery = remember { mutableStateOf(QueryStatus.Querying) }
    val notes = remember { mutableListOf<Note>() }
    val scope = rememberCoroutineScope()

    suspend fun loadUserData() {
        userQuery.value = UserQuery.Querying
        account.getDetailedUserData().fold(
            { userQuery.value = UserQuery.Success(it) },
            { userQuery.value = UserQuery.Error(it) }
        )
    }

    suspend fun loadNotes() {
        noteQuery.value = QueryStatus.Querying
        account.getNotesTimeline().fold(
            {
                notes.addAll(it)
                noteQuery.value = QueryStatus.Success
            },
            {
                noteQuery.value = QueryStatus.Error
            }
        )
    }

    LaunchedEffect(Unit) {
        launch { loadUserData() }
        launch { loadNotes() }
    }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = { Drawer(account, accountData, addAccount) },
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
                Column(
                    modifier = Modifier.padding(padding)
                ) {
            }
        }
    }

}