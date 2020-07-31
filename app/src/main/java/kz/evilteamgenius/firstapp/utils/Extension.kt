package kz.evilteamgenius.firstapp.utils

import android.os.StrictMode
import android.view.View
import kz.evilteamgenius.firstapp.models.Currency
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.net.URL
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory


fun loadData(isMainRates: Boolean = false): ArrayList<Currency> {
    val arr = ArrayList<Currency>()
    try {
        val url = if (isMainRates) {
            URL(Constants.mainRate)
        } else {
            URL(Constants.allRates)
        }

        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc: Document = db.parse(InputSource(url.openStream()))
        doc.documentElement.normalize()
        val nodeList1: NodeList = doc.getElementsByTagName("item")
        for (i in 0 until nodeList1.length) {
            val currency = Currency()
            try {
                val node = nodeList1.item(i) as Element
                val title = // USD
                    node.getElementsByTagName("title").item(0).childNodes.item(0).nodeValue
                currency.title = title
                val pubDate =  // 29.06.2020
                    node.getElementsByTagName("pubDate").item(0).firstChild.nodeValue;
                currency.pubDate = pubDate
                val description = // USD = 401kzt
                    node.getElementsByTagName("description").item(0).firstChild.nodeValue;
                currency.description = description

                val change = // +-0.5/+-122
                    node.getElementsByTagName("change").item(0).firstChild.nodeValue;
                currency.change = change
                val index : String = //up and down
                    node.getElementsByTagName("index").item(0).childNodes.item(0).nodeValue
                currency.index = index
                println(currency.toString())
                arr.add(currency)
            }catch (e : java.lang.Exception){
                currency.index=""
                println(currency.toString())
                arr.add(currency)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println("LOAD FINISHED")
    return arr
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun enablePolicy() {
    val policy: StrictMode.ThreadPolicy =
        StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
}

