package kz.evilteamgenius.firstapp.viewModel

import android.os.AsyncTask
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.evilteamgenius.firstapp.database.CurrencyDB
import kz.evilteamgenius.firstapp.models.Currency
import kz.evilteamgenius.firstapp.utils.DBUtil
import kz.evilteamgenius.firstapp.utils.loadData

class MainViewModel : ViewModel() {
    val allCurrency = MutableLiveData<List<Currency>>()
    val allCurrencyDB = MutableLiveData<List<CurrencyDB>>()
    val mainCurrencyData = MutableLiveData<List<Currency>>()
    val db = DBUtil()

    fun loadAllData() {
        allCurrency.value = loadData()
        changeValue()
        val handler = Handler()
        val delay = 30 * 1000 //milliseconds
        try {
            handler.postDelayed(object : Runnable {
                override fun run() {
                    //do something
                    allCurrency.value = loadData()
                    changeValue()
                    handler.postDelayed(this, delay.toLong())
                }
            }, delay.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeValue() {
        val a = AsyncTask.execute {
            db.getDB().userDao().deleteAll()
            allCurrency.value?.forEach {
                db.getDB().userDao().insertAll(initCurrency(it))
            }

            val allCur = ArrayList<Currency>()
            val mainCur = ArrayList<Currency>()
            allCurrency.value?.forEach {
                allCur.add(it)
                if (checkForMain(it)) {
                    mainCur.add(it)
                }
            }
            kotlin.run {
                allCurrencyDB.postValue(db.getDB().userDao().getAll())
                allCurrency.postValue(allCur)
                mainCurrencyData.postValue(mainCur)
            }
        }


    }

    private fun checkForMain(currency: Currency): Boolean {
        when (currency.title) {
            "USD", "RUB", "EUR", "GBP" -> {
                return true
            }
        }
        return false
    }

    private fun initCurrency(currencyDB: CurrencyDB): Currency {
        val currency = Currency()
        currencyDB.change?.let { currency.change = it }
        currencyDB.description?.let { currency.description = it }
        currencyDB.index?.let { currency.index = it }
        currencyDB.pubDate?.let { currency.pubDate = it }
        currencyDB.title?.let { currency.title = it }
        return currency
    }

    private fun initCurrency(currencyDB: Currency): CurrencyDB {
        return CurrencyDB(
            change = currencyDB.change,
            title = currencyDB.title,
            pubDate = currencyDB.pubDate,
            description = currencyDB.description,
            index = currencyDB.index
        )
    }
}