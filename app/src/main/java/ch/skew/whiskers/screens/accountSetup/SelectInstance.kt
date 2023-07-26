package ch.skew.whiskers.screens.accountSetup

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ch.skew.whiskers.components.LabelledRadioButton

@Preview
@Composable
fun SelectInstancePreview() {
    SelectInstance {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInstance(
    goBack: () -> Unit
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
        Column(
            modifier = Modifier.padding(padding)
        ) {
            LabelledRadioButton(label = "Use popular instances", selected = !customUrl.value) {
                customUrl.value = false
            }
            LabelledRadioButton(label = "Enter instance URL", selected = customUrl.value) {
                customUrl.value = true
            }
            if (customUrl.value) {

            } else {

            }
        }
    }
}