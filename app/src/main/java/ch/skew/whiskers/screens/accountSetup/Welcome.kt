package ch.skew.whiskers.screens.accountSetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Column(
            modifier = Modifier.padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier.weight(1F)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(id = R.string.app_name),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.weight(1F)
                    .fillMaxHeight()
            ) {
                Text(
                    "Android app for Misskey/Firefish that aims to be highly customisable",
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier.weight(3F)
                    .fillMaxHeight()
            ) {
                Button(
                    onClick = {}
                ) {
                    Text("Get Started")
                }
            }
        }
    }
}