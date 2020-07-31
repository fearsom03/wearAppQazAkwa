package kz.evilteamgenius.firstapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.evilteamgenius.firstapp.database.CurrencyDB
import kz.evilteamgenius.firstapp.models.Currency
import kz.evilteamgenius.firstapp.utils.DBUtil
import kz.evilteamgenius.firstapp.utils.loadData

class MainViewModel : ViewModel() {
    // I'm too lazy to seperate the methods but is is possible )))
    val allCurrency = MutableLiveData<List<Currency>>()
    var allCurrencyDB = MutableLiveData<List<CurrencyDB>>()
    val mainCurrencyData = MutableLiveData<List<Currency>>()
    val db = DBUtil()
    val isInternetActive = MutableLiveData<Boolean>()

    init {
        isInternetActive.value = true
    }

    suspend fun loadAllData() = viewModelScope.launch {
        if (isInternetActive.value!!) {
            allCurrency.value = loadData()
            changeValue()
            loadDataSomeTime()
        }
    }


    private suspend fun loadDataSomeTime() = viewModelScope.launch {
        try {
            // seconds * milliseconds like 35 * 1 (000)
            val delayTime: Long = 35 * 1000
            while (isInternetActive.value!!) {
                delay(delayTime)
                allCurrency.value = loadData()
                changeValue()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeValue() = viewModelScope.launch {

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
            allCurrencyDB.value = db.getDB().userDao().getAll().value
            allCurrency.postValue(allCur)
            mainCurrencyData.postValue(mainCur)
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