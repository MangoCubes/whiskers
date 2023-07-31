package ch.skew.whiskers.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WhiskersSettings(
    private val context: Context
) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        val lastAccount = stringPreferencesKey("lastAccount")
    }

    val getLastAccount: Flow<Int?> = context.dataStore.data.map {
        it[lastAccount]?.toInt()
    }

    suspend fun saveLastAccount(accountId: Int) {
        context.dataStore.edit {
            it[lastAccount] = accountId.toString()
        }
    }
}