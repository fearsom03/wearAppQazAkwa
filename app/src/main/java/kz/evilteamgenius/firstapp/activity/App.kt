package kz.evilteamgenius.firstapp.activity

import android.app.Application
import androidx.room.Room
import kz.evilteamgenius.firstapp.database.AppDatabase

class App : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (database == null)
            database = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "qazAkwa.db"
            ).allowMainThreadQueries()
                .build()
    }
}