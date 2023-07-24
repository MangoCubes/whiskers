package ch.skew.whiskers.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_data")
data class AccountData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "token")
    val token: String
)