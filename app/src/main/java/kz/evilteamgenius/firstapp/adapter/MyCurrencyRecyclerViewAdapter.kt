package kz.evilteamgenius.firstapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.models.Currency

class MyCurrencyRecyclerViewAdapter() :
    RecyclerView.Adapter<MyCurrencyRecyclerViewAdapter.ViewHolder>() {

    constructor(it: List<Currency>) : this() {
        values = it
    }

    private lateinit var values: List<Currency>

    fun setValues(it: List<Currency>) {
        values = it
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.title
        holder.contentView.text = item.description
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}