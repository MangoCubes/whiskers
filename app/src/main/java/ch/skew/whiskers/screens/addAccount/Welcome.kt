package ch.skew.whiskers.screens.addAccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.skew.whiskers.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Welcome() {
    Scaffold(
        topBar = {
            TopAppBar({
                Text(stringResource(R.string.welcome))
            })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            
        }

    }
}