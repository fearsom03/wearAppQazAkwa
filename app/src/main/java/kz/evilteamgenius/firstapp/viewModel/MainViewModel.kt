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
import timber.log.Timber

class MainViewModel : ViewModel() {
    // I'm too lazy to separate the methods but is is possible )))
    val allCurrency = MutableLiveData<List<Currency>>()
    var allCurrencyDB = MutableLiveData<List<CurrencyDB>>()
    val mainCurrencyData = MutableLiveData<List<Currency>>()
    val db = DBUtil()
    val isInternetActive = MutableLiveData<Boolean>()

    init {
        isInternetActive.value = true
    }

    suspend fun loadAllData() {
        kotlin.run {
            if (isInternetActive.value!!) {
                val a = loadData()
                if (a.isNotEmpty()) {
                    allCurrency.postValue(a)
                    changeValue()
                }
                loadDataSomeTime()
            }
        }

    }


    private suspend fun loadDataSomeTime() {
        try {
            val second: Long = 1000
            // seconds * milliseconds like 35 * 1 (000)
            val delayTime: Long = 500 * second
            while (isInternetActive.value!!) {
                delay(delayTime)
                val a = loadData()
                if (a.isNotEmpty()) {
                    allCurrency.value = a
                    changeValue()
                } else {
                    isInternetActive.value = false
                    getOffileData()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isInternetActive.value = false
            getOffileData()
        }
    }

    private suspend fun changeValue() = viewModelScope.launch {

        db.getCurrencyDb().deleteAll()
        allCurrency.value?.forEach {
            db.getCurrencyDb().insertAll(initCurrency(it))
        }
        val allCur = ArrayList<Currency>()
        val mainCur = ArrayList<Currency>()
        allCurrency.value?.forEach {
            allCur.add(it)
            if (checkForMain(it)) {
                mainCur.add(it)
            }
        }
        Timber.d("allcur kuka ->$allCur")
        allCurrencyDB.value = db.getCurrencyDb().getAll()
        allCurrency.value = (allCur)
        mainCurrencyData.value = (mainCur)
    }

    suspend fun getOffileData() = viewModelScope.launch {
        val allCur = ArrayList<Currency>()
        val mainCur = ArrayList<Currency>()
        allCurrencyDB.value = db.getCurrencyDb().getAll()

        allCurrencyDB.value?.forEach { cur ->
            val it = initCurrency(cur)
            allCur.add(it)
            if (checkForMain(it)) {
                mainCur.add(it)
            }
        }
        allCurrency.value = (allCur)
        mainCurrencyData.value = (mainCur)
    }

    private fun checkForMain(currency: Currency): Boolean {
        when (currency.title) {
            "USD", "RUB", "EUR", "GBP" -> {
                return true
            }
        }
        return false
    }

    fun initCurrency(currencyDB: CurrencyDB): Currency {
        val currency = Currency()
        currencyDB.change?.let { currency.change = it }
        currencyDB.description?.let { currency.description = it }
        currencyDB.index?.let { currency.index = it }
        currencyDB.pubDate?.let { currency.pubDate = it }
        currencyDB.title?.let { currency.title = it }
        return currency
    }

    fun initCurrency(currencyDB: Currency): CurrencyDB {
        return CurrencyDB(
            change = currencyDB.change,
            title = currencyDB.title,
            pubDate = currencyDB.pubDate,
            description = currencyDB.description,
            index = currencyDB.index
        )
    }
}