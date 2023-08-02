package ch.skew.whiskers.screens.accountSetup

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.data.AuthInProgress
import ch.skew.whiskers.data.WhiskersSettings
import ch.skew.whiskers.misskey.MisskeyLoginClient
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
@Preview
fun LoginPreview() {
    Login("https://misskey.io") {}
}

enum class LoginState {
    Idle,
    CreatingApp,
    GeneratingSession,
    Redirecting,
    Error
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    host: String?,
    goBack: () -> Unit,
) {
    if (host === null) goBack()
    else {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val uriHandler = LocalUriHandler.current
        Scaffold(
            topBar = {
                TopAppBar(
                    { Text("Login") },
                    navigationIcon = {
                        IconButton(goBack) {
                            Icon(Icons.Filled.ArrowBack, "Go back")
                        }
                    }
                )
            },
        ) { padding ->
            val state = remember { mutableStateOf(LoginState.Idle) }
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    painter = rememberAsyncImagePainter(
                        Uri.Builder()
                            .scheme("https")
                            .authority(host)
                            .appendPath("static-assets")
                            .appendPath("icons")
                            .appendPath("192.png")
                            .build()
                            .toString()
                    ),
                    contentDescription = host
                )
                Button(
                    onClick = {
                        scope.launch {
                            val uri = Uri.Builder()
                                .scheme("https")
                                .authority(host)
                                .build()
                            state.value = LoginState.CreatingApp
                            MisskeyLoginClient.create(uri).fold({
                                state.value = LoginState.GeneratingSession
                                val generated = it.generate().getOrNull()
                                if (generated !== null) {
                                    state.value = LoginState.Redirecting
                                    val settings = WhiskersSettings(context)
                                    settings.saveAuthInProgress(
                                        AuthInProgress(it.appSecret, generated.token, it.instance)
                                    )
                                    uriHandler.openUri(generated.url)
                                    state.value = LoginState.Idle
                                } else {
                                    state.value = LoginState.Error
                                }
                            }, {
                                state.value = LoginState.Error
                            })
                        }
                    },
                    enabled = state.value === LoginState.Idle || state.value === LoginState.Error
                ) {
                    Text("Login")
                }
                Text(
                    when(state.value) {
                        LoginState.Idle -> ""
                        LoginState.CreatingApp -> "Creating app..."
                        LoginState.GeneratingSession -> "Generating session..."
                        LoginState.Redirecting -> "Redirecting..."
                        LoginState.Error -> "Login failed!"
                    }
                )
            }
        }
    }
}