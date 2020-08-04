package kz.evilteamgenius.firstapp.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.adapter.MyCurrencyRecyclerViewAdapter
import kz.evilteamgenius.firstapp.viewModel.MainViewModel

/**
 * A fragment representing a list of Items.
 */
class ItemFragment(val type: Int = 0) : BaseFragment(R.layout.fragment_item_list) {

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
                val myAdapter = MyCurrencyRecyclerViewAdapter()
                launch {
                    viewModel.getOffileData()
                }
                when (type) {
                    0 -> {
                        viewModel.allCurrency.observe(viewLifecycleOwner, Observer {
                            it?.let {
                                myAdapter.setValues(it)
                                adapter = myAdapter
                            }
                        })
                    }
                    1 -> {
                        viewModel.mainCurrencyData.observe(viewLifecycleOwner, Observer {
                            it?.let {
                                myAdapter.setValues(it)
                                adapter = myAdapter
                            }
                        })
                    }
                }
            }
        }

    }
}