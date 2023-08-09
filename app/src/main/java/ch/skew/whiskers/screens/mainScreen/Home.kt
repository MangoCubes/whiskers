package ch.skew.whiskers.screens.mainScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.skew.whiskers.classes.DataQueryStatus
import ch.skew.whiskers.classes.ErrorQueryStatus
import ch.skew.whiskers.components.PullRefreshIndicator
import ch.skew.whiskers.data.accounts.AccountData
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.misskey.MisskeyClient
import ch.skew.whiskers.misskey.data.Note
import ch.skew.whiskers.misskey.data.UserDetailed
import ch.skew.whiskers.misskey.data.api.Emoji
import ch.skew.whiskers.misskey.error.api.AuthenticationError
import ch.skew.whiskers.misskey.error.api.ClientError
import ch.skew.whiskers.misskey.error.api.ForbiddenError
import ch.skew.whiskers.misskey.error.api.InternalServerError
import ch.skew.whiskers.misskey.error.api.NotFoundError
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Home(
    accountData: List<AccountData>,
    account: MisskeyClient,
    addAccount: () -> Unit,
    selectAccount: (String, String) -> Boolean
) {
    val userQuery = remember { mutableStateOf<UserQuery>(UserQuery.Querying) }
    val notesQuery = remember { mutableStateOf<ErrorQueryStatus>(ErrorQueryStatus.Querying(false)) }
    val notes = remember { mutableListOf<Note>() }
    val emojis = remember { mutableStateOf<DataQueryStatus<Map<String, Emoji>>>(DataQueryStatus.Querying(false)) }
    val emojiContent = remember { mutableMapOf<String, InlineTextContent>() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
                notesQuery.value = ErrorQueryStatus.Success
                notes.clear()
                notes.addAll(it)
            },
            {
                notesQuery.value = ErrorQueryStatus.Error(it)
            }
        )
    }

    suspend fun reloadNotes() {
        notesQuery.value = ErrorQueryStatus.Querying(true)
        loadNotes()
    }

    LaunchedEffect(account) {
        emojis.value = DataQueryStatus.Querying(false)
        emojiContent.clear()
        notes.clear()
        launch {
            account.getEmojis().onSuccess { res ->
                emojis.value = DataQueryStatus.Success(
                    res.emojis.associateBy {
                        it.name
                    }
                )
            }.onFailure {
                // Emojis may not exist in a given instance
                println(it)
                if (it is NotFoundError) emojis.value = DataQueryStatus.Success(mapOf())
            }
        }
        launch {
            userQuery.value = UserQuery.Querying
            loadUserData()
        }
        launch {
            notesQuery.value = ErrorQueryStatus.Querying(false)
            loadNotes()
        }
    }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                val refreshing = notesQuery.value is ErrorQueryStatus.Querying
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = refreshing,
                    onRefresh = { scope.launch { reloadNotes() } }
                )
                PullRefreshIndicator(state = pullRefreshState, refreshing = refreshing)
                notesQuery.value.let { notesStatus ->
                    when(notesStatus) {
                        is ErrorQueryStatus.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    when(notesStatus.error) {
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
                                        onClick = {
                                            scope.launch { reloadNotes() }
                                            scope.launch { loadUserData() }
                                        }
                                    ) {
                                        Text("Reload")
                                    }
                                }
                            }
                        }
                        else -> {
                            emojis.value.let { emojiMap ->
                                if(emojiMap is DataQueryStatus.Success) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .pullRefresh(pullRefreshState),
                                        userScrollEnabled = true
                                    ) {
                                        items (
                                            count = notes.size,
                                            key = {
                                                notes[it].id
                                            }
                                        ) { index ->
                                            NoteCard(notes[index], {}, emojiContent) { emojiList ->
                                                emojiList.forEach {
                                                    val emoji = emojiMap.item[it] ?: return@forEach
                                                    scope.launch {
                                                        val image = context.imageLoader.execute(
                                                            ImageRequest.Builder(context)
                                                                .data(emoji.url)
                                                                .memoryCacheKey(emoji.name)
                                                                .diskCacheKey(emoji.name)
                                                                .diskCachePolicy(CachePolicy.ENABLED)
                                                                .memoryCachePolicy(CachePolicy.ENABLED)
                                                                .crossfade(true)
                                                                .build()
                                                        )
                                                        emojiContent[emoji.name] = InlineTextContent(
                                                            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
                                                        ) {
                                                            AsyncImage(model = image, contentDescription = emoji.name)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}