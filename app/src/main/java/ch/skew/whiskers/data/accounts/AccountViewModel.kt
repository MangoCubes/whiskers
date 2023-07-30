package ch.skew.whiskers.data.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
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
                    dao.delete(e.id)
                }
            }
            is AccountEvent.ActivateAccount -> {
                viewModelScope.launch {
                    dao.activate(e.id, e.url, e.appSecret, e.token)
                }
            }
            is AccountEvent.SaveAccessToken -> {
                viewModelScope.launch {
                    dao.saveAccessToken(e.id, e.accessToken)
                }
            }
        }
    }

    fun onEventAsync(e: AccountEventAsync): Deferred<Long> {
        when(e) {
            is AccountEventAsync.InsertAccountAsync -> {
                return viewModelScope.async {
                    return@async dao.insert()
                }
            }
        }
    }
}