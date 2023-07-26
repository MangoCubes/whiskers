package ch.skew.whiskers.screens.accountSetup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LoginPreview() {
    Login("https://misskey.io") {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    instanceUrl: String?,
    goBack: () -> Unit
) {
    if (instanceUrl === null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("You are not supposed to see this, go away.")
        }
    } else {
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
            Column(
                modifier = Modifier.padding(padding)
            ) {
            }
        }
    }
}