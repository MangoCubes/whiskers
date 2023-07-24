package ch.skew.whiskers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.skew.whiskers.data.WhiskersDB
import ch.skew.whiskers.data.accounts.AccountDataViewModel
import ch.skew.whiskers.screens.addAccount.Welcome
import ch.skew.whiskers.screens.loading.Router
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
                    val accounts = accountDataViewModel.accounts.collectAsState()
                    if (accounts.value.isEmpty()) {
                        Welcome()
                    } else {
                        Router(accounts.value)
                    }
                }
            }
        }
    }
}