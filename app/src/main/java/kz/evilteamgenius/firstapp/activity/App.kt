package kz.evilteamgenius.firstapp.activity

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import kz.evilteamgenius.firstapp.BuildConfig
import kz.evilteamgenius.firstapp.database.AppDatabase
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    companion object {
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        if (database == null) {
            database = Room.databaseBuilder(
                this,
                AppDatabase::class.java, "qazAkwa.db"
            ).allowMainThreadQueries()
                .build()
        }

        FirebaseAnalytics.getInstance(this)

        Timber.plant(if (BuildConfig.DEBUG) DebugTree() else CrashReportingTree(applicationContext))

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private class CrashReportingTree(val context: Context) : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            throwable: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            val params = Bundle()
            params.putString("message", message)
            val mFirebaseAnalytics = FirebaseAnalytics
                .getInstance(context)
            mFirebaseAnalytics.logEvent(tag!!, params)
        }
    }
}