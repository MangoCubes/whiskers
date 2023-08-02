package ch.skew.whiskers.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "account_data", primaryKeys = ["host", "username"])
data class AccountData(
    @ColumnInfo(name = "host")
    val host: String,
    @ColumnInfo(name = "app_secret")
    val appSecret: String,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "username")
    val username: String
)