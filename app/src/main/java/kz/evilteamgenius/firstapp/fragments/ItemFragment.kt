package kz.evilteamgenius.firstapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.adapter.MyCurrencyRecyclerViewAdapter
import kz.evilteamgenius.firstapp.viewModel.MainViewModel

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment(R.layout.fragment_item_list) {

    private var columnCount = 1
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                viewModel.allCurrency.observe(viewLifecycleOwner, Observer {
                    adapter = MyCurrencyRecyclerViewAdapter(it)
                })
            }
        }
    }
}