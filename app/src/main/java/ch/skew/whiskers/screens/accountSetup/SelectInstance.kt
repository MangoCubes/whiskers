package ch.skew.whiskers.screens.accountSetup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.skew.whiskers.components.LabelledRadioButton
import coil.compose.rememberAsyncImagePainter

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
    onSelect: () -> Unit
) {
    val wellKnown = listOf(
        WellKnownInstances("https://misskey.io", "Misskey", "Most popular Misskey instance"),
        WellKnownInstances("https://calckey.world", "Calckey.world", "Well known Firefish instance")
    )
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
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxHeight()
                .verticalScroll(scroll)
        ) {
            LabelledRadioButton(label = "Use popular instances", selected = !customUrl.value) {
                customUrl.value = false
            }
            LabelledRadioButton(label = "Enter instance URL", selected = customUrl.value) {
                customUrl.value = true
            }
            if (customUrl.value) {

            } else {
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
                                painter = rememberAsyncImagePainter(it.url + "/static-assets/icons/192.png"),
                                contentDescription = it.name
                            )
                        }
                    )
                }
            }
        }
    }
}