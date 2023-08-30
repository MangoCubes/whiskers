package ch.skew.whiskers.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.Index
import androidx.room.PrimaryKey
import ch.skew.whiskers.data.settings.Settings

@Entity(
    tableName = "account_data",
    indices = [Index(value = ["host", "username"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Settings::class,
            parentColumns = ["id"],
            childColumns = ["settings"],
            onDelete = SET_NULL
        )
    ]
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
    val username: String,
    @ColumnInfo(name = "settings")
    val settings: Int? = null
)