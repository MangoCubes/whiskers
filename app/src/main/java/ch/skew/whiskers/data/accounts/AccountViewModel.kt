package ch.skew.whiskers.data.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AccountDataViewModel(
    private val dao: AccountDataDao
): ViewModel() {
    val accounts = dao.getAllAccounts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun onEventAsync(e: AccountEvent): Deferred<Throwable?> {
        when(e) {
            is AccountEvent.DeleteAccount -> {
                return viewModelScope.async {
                    dao.delete(e.host, e.username)
                    return@async null
                }
            }
            is AccountEvent.AddAccount -> {
                return viewModelScope.async {
                    try {
                        dao.insert(e.host, e.appSecret, e.accessToken, e.username)
                        return@async null
                    } catch (e: Throwable) {
                        return@async e
                    }
                }
            }
        }
    }
}