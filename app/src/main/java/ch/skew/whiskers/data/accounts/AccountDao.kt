package ch.skew.whiskers.data.accounts

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AccountDao{
    @Query("INSERT INTO account_data (username, url, token) VALUES (:username, :url, :token)")
    suspend fun insert(username: String, url: String, token: String)
    @Query("SELECT * FROM account_data WHERE id = :id")
    suspend fun getAccountInfo(id: Int): List<AccountData>
    @Query("DELETE FROM account_data WHERE id = :id")
    suspend fun delete(id: Int)
}