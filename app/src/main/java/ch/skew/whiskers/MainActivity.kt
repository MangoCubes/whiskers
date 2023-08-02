package ch.skew.whiskers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import ch.skew.whiskers.data.WhiskersDB
import ch.skew.whiskers.data.accounts.AccountDataViewModel
import ch.skew.whiskers.data.accounts.AccountEvent
import ch.skew.whiskers.screens.accountSetup.accountSetup
import ch.skew.whiskers.screens.mainScreen.main
import ch.skew.whiskers.ui.theme.WhiskersTheme

class MainActivity : ComponentActivity() {

    @Suppress("UNCHECKED_CAST")
    private val accountDataViewModel by viewModels<AccountDataViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AccountDataViewModel(WhiskersDB.getInstance(applicationContext).accountDataDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhiskersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val accounts = accountDataViewModel.accounts.collectAsState(null)
                    val nav = rememberNavController()
                    accounts.value?.let {
                        val initial = remember {
                            mutableStateOf(it.isEmpty())
                        }
                        NavHost(
                            navController = nav,
                            startDestination = if (initial.value) Pages.Graphs.AccountSetup.route else Pages.Graphs.Main.route
                        ) {
                            navigation(
                                route = Pages.Graphs.AccountSetup.route,
                                startDestination = Pages.AccountSetup.Welcome.route
                            ){
                                accountSetup(nav)
                            }
                            navigation(
                                route = Pages.Graphs.Main.route,
                                startDestination = Pages.Main.Home.route
                            ){
                                main(
                                    nav,
                                    it,
                                    { nav.navigate(Pages.AccountSetup.SelectInstance.route) },
                                    { host, appSecret, accessToken, username ->
                                        accountDataViewModel.onEvent(
                                            AccountEvent.AddAccount(host, appSecret, accessToken, username)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
/**
 * TODO List:
 * Give every Text component something from MaterialTheme.typography
 * Make every string use resource
 */