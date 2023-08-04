package ch.skew.whiskers.data.accounts

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDataDao{
    @Query("INSERT INTO account_data (host, app_secret, access_token, username) VALUES (:host, :appSecret, :token, :username)")
    suspend fun insert(host: String, appSecret: String, token: String, username: String)
    @Query("SELECT * FROM account_data")
    fun getAllAccounts(): Flow<List<AccountData>>
    @Query("SELECT * FROM account_data WHERE host = :host AND username = :username")
    fun getAccountInfo(host: String, username: String): Flow<AccountData>
    @Query("DELETE FROM account_data WHERE host = :host AND username = :username")
    suspend fun delete(host: String, username: String)
}