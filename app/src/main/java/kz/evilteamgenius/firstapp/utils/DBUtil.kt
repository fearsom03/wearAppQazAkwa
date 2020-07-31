package kz.evilteamgenius.firstapp.utils

import kz.evilteamgenius.firstapp.activity.App
import kz.evilteamgenius.firstapp.database.AppDatabase
import kz.evilteamgenius.firstapp.database.CurrencyDao

class DBUtil() {
    private var appDatabase: AppDatabase = App.database!!

    fun getDB(): AppDatabase {
        return appDatabase
    }

    fun getCurrencyDb(): CurrencyDao? {
        return appDatabase.userDao()
    }

    fun closeDB() {
        appDatabase.close()
    }
}