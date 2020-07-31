package kz.evilteamgenius.firstapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencydb")
    fun getAll(): LiveData<List<CurrencyDB>>

    @Query("SELECT * FROM currencydb WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<CurrencyDB>

    @Query("SELECT * FROM currencydb WHERE title LIKE :name")
    fun findByName(name: String): CurrencyDB

    @Insert
    fun insertAll(vararg currs: CurrencyDB)

    @Delete
    fun delete(curr: CurrencyDB)

    @Query("delete  From currencydb")
    fun deleteAll()
}