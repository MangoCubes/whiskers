package ch.skew.whiskers.data.accounts

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDataDao{
    @Query("INSERT INTO account_data (username, url, token) VALUES (:username, :url, :token)")
    suspend fun insert(username: String, url: String, token: String)
    @Query("SELECT * FROM account_data")
    fun getAllAccounts(): Flow<List<AccountData>>
    @Query("SELECT * FROM account_data WHERE id = :id")
    fun getAccountInfo(id: Int): Flow<AccountData>
    @Query("DELETE FROM account_data WHERE id = :id")
    suspend fun delete(id: Int)
}