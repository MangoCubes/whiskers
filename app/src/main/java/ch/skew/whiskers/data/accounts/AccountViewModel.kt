package ch.skew.whiskers.data.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountDataViewModel(
    private val dao: AccountDataDao
): ViewModel() {
    val accounts = dao.getAllAccounts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun onEvent(e: AccountEvent) {
        when(e) {
            is AccountEvent.DeleteAccount -> {
                viewModelScope.launch {
                    dao.delete(e.host, e.username)
                }
            }
            is AccountEvent.AddAccount -> {
                viewModelScope.launch {
                    dao.insert(e.host, e.appSecret, e.accessToken, e.username)
                }
            }
        }
    }
}