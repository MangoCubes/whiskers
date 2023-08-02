package ch.skew.whiskers.screens.mainScreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.misskey.MisskeyClient
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
    Home(MisskeyClient("", MisskeyAPI(Uri.parse("")), ""))
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    account: MisskeyClient
) {
    val query = remember { mutableStateOf<UserQuery>(UserQuery.Querying) }
    val scope = rememberCoroutineScope()

    suspend fun loadUserData() {
        query.value = UserQuery.Querying
        account.getDetailedUserData().fold(
            { query.value = UserQuery.Success(it) },
            { query.value = UserQuery.Error(it) }
        )
    }

    LaunchedEffect(Unit) {
        loadUserData()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                { ListItem(
                    headlineText = { Text("Home") },
                    supportingText = { Text(account.username) },
                    leadingContent = {
                        query.value.let {
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
                    },
                    modifier = Modifier.clickable { }
                ) },
                navigationIcon = {
                    IconButton({}) {
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