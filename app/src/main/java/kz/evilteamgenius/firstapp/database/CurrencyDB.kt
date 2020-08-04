package kz.evilteamgenius.firstapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyDB(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "pubDate") val pubDate: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "index") val index: String?,
    @ColumnInfo(name = "change") val change: String?

) {
    override fun toString(): String {
        return "CurrencyDB(uid=$uid, title=$title, pubDate=$pubDate, description=$description, index=$index, change=$change)"
    }
}
