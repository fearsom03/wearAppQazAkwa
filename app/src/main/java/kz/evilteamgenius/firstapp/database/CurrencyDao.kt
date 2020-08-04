package kz.evilteamgenius.firstapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencydb")
    suspend fun getAll(): List<CurrencyDB>

    @Query("SELECT * FROM currencydb WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<CurrencyDB>

    @Query("SELECT * FROM currencydb WHERE title LIKE :name")
    suspend fun findByName(name: String): CurrencyDB

    @Insert
    suspend fun insertAll(vararg currs: CurrencyDB)

    @Delete
    suspend fun delete(curr: CurrencyDB)

    @Query("delete  From currencydb")
    suspend fun deleteAll()
}