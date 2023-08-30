package ch.skew.whiskers.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_data",
    indices = [Index(value = ["host", "username"], unique = true)]
)
data class AccountData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "host")
    val host: String,
    @ColumnInfo(name = "app_secret")
    val appSecret: String,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "username")
    val username: String
)