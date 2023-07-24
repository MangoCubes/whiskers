package ch.skew.whiskers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.skew.whiskers.data.accounts.AccountDataDao
import ch.skew.whiskers.data.accounts.AccountData


@Database(
    entities = [
        AccountData::class,
    ],
    version = 2
)

abstract class WhiskersDB: RoomDatabase(){
    abstract val accountDataDao: AccountDataDao
    companion object {

        @Volatile
        private var instance: WhiskersDB? = null

        fun getInstance(context: Context): WhiskersDB {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, WhiskersDB::class.java, "whiskers.db")
                .build()
    }
}