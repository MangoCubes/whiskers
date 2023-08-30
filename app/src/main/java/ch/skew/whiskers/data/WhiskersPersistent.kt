package ch.skew.whiskers.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AuthInProgress(
    val appSecret: String,
    val token: String,
    val host: String
)
class WhiskersPersistent(
    private val context: Context
) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "persistent")
        val lastAccount = stringPreferencesKey("lastAccount")
        val tempAppSecret = stringPreferencesKey("tempAppSecret")
        val tempToken = stringPreferencesKey("tempToken")
        val tempHost = stringPreferencesKey("tempHost")
    }

    val getLastAccount: Flow<Int?> = context.dataStore.data.map {
        it[lastAccount]?.toInt()
    }

    val getAuthInProgress: Flow<AuthInProgress?> = context.dataStore.data.map {
        val appSecret = it[tempAppSecret]
        val token = it[tempToken]
        val host = it[tempHost]
        if(appSecret !== null && token !== null && host !== null) AuthInProgress(appSecret, token, host)
        else null
    }

    suspend fun saveLastAccount(accountId: Int) {
        context.dataStore.edit {
            it[lastAccount] = accountId.toString()
        }
    }

    suspend fun saveAuthInProgress(auth: AuthInProgress) {
        context.dataStore.edit {
            it[tempAppSecret] = auth.appSecret
            it[tempToken] = auth.token
            it[tempHost] = auth.host
        }
    }
}