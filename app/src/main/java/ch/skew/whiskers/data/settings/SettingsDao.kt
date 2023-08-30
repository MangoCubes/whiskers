package ch.skew.whiskers.data.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SettingsDao{
    @Insert
    suspend fun insert(settings: Settings)
    @Update
    suspend fun update(settings: Settings)

    @Query("SELECT * FROM settings WHERE id = :id")
    suspend fun getSettings(id: Int): Settings
    @Query("DELETE FROM settings WHERE id = :id")
    suspend fun delete(id: Int)
}