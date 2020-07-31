package kz.evilteamgenius.firstapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [CurrencyDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): CurrencyDao
}