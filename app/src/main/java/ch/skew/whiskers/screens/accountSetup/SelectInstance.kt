package ch.skew.whiskers.screens.accountSetup

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.classes.QueryStatus
import ch.skew.whiskers.misskey.MisskeyAPI
import ch.skew.whiskers.components.LabelledRadioButton
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

data class WellKnownInstances(
    val url: String,
    val name: String,
    val desc: String
)

@Preview
@Composable
fun SelectInstancePreview() {
    SelectInstance({}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInstance(
    goBack: () -> Unit,
    onSelect: (url: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                { Text("Select Instance") },
                navigationIcon = {
                    IconButton(goBack) {
                        Icon(Icons.Filled.ArrowBack, "Go back")
                    }
                }
            )
        },

    ) { padding ->
        val customUrl = remember { mutableStateOf(false) }
        val instanceUrl = remember { mutableStateOf(
            Uri.Builder()
                .scheme("https")
                .authority("misskey.io")
                .build()
        ) }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
        ) {
            LabelledRadioButton(label = "Use popular instances", selected = !customUrl.value) {
                customUrl.value = false
            }
            LabelledRadioButton(label = "Enter instance URL", selected = customUrl.value) {
                customUrl.value = true
            }
            if (customUrl.value) {
                val scope = rememberCoroutineScope()
                val tempUrl = remember { mutableStateOf("") }
                val status = remember { mutableStateOf(QueryStatus.Success) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(
                            instanceUrl.value
                                .buildUpon()
                                .appendPath("static-assets")
                                .appendPath("icons")
                                .appendPath("192.png")
                                .build()
                                .toString()
                        ),
                        contentDescription = instanceUrl.value.toString()
                    )
                    Text(
                        instanceUrl.value.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = when(status.value) {
                            QueryStatus.Querying -> Color.Gray
                            QueryStatus.Error -> Color.Red
                            QueryStatus.Success -> Color.Green
                        }
                    )
                    TextField(
                        value = tempUrl.value,
                        onValueChange = { tempUrl.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                val instance = Uri.Builder()
                                    .scheme("https")
                                    .authority(tempUrl.value)
                                    .build()
                                instanceUrl.value = instance
                                scope.launch {
                                    status.value = QueryStatus.Querying
                                    status.value =
                                        if (MisskeyAPI.ping(instance.toString())) QueryStatus.Success
                                        else QueryStatus.Error
                                }
                            }
                        ) {
                            Text("Test")
                        }
                        Button(
                            onClick = {
                                val url = Uri.parse(tempUrl.value)
                                onSelect(url.authority ?: "localhost")
                            },
                            enabled = status.value !== QueryStatus.Querying
                        ) {
                            Text("Continue")
                        }
                    }

                }
            } else {
                val wellKnown = listOf(
                    WellKnownInstances("misskey.io", "Misskey", "Most popular Misskey instance"),
                    WellKnownInstances("calckey.world", "Calckey.world", "Well known Firefish instance"),
                    WellKnownInstances("misskey.design", "Misskey Design", "Misskey instance for artists")
                )
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scroll)
                ) {
                    wellKnown.forEach {
                        ListItem(
                            headlineText = { Text(it.name) },
                            supportingText = { Text(it.desc) },
                            overlineText = { Text(it.url) },
                            leadingContent = {
                                Image(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape),
                                    painter = rememberAsyncImagePainter(
                                        Uri.Builder()
                                            .scheme("https")
                                            .authority(it.url)
                                            .appendPath("static-assets")
                                            .appendPath("icons")
                                            .appendPath("192.png")
                                            .build()
                                            .toString()
                                    ),
                                    contentDescription = it.name
                                )
                            },
                            modifier = Modifier.clickable {
                                onSelect(it.url)
                            }
                        )
                    }
                }
            }
        }
    }
}