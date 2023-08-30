package ch.skew.whiskers.data.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dao: SettingsDao
): ViewModel() {
    fun onEvent(e: SettingsEvent) {
        when(e) {
            is SettingsEvent.AddSettings -> {
                viewModelScope.launch {
                    dao.insert(e.settings)
                }
            }
            is SettingsEvent.DeleteSettings -> {
                viewModelScope.launch {
                    dao.delete(e.id)
                }
            }
            is SettingsEvent.UpdateSettings -> {
                viewModelScope.launch {
                    dao.update(e.settings)
                }
            }
        }
    }
}