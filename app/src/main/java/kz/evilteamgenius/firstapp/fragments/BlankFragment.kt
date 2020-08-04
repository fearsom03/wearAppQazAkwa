package kz.evilteamgenius.firstapp.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_blank.*
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.viewModel.MainViewModel

class BlankFragment : BaseFragment(R.layout.fragment_blank) {
    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        someText.setOnClickListener {
            findNavController().navigate(R.id.action_blankFragment_to_itemFragment)
        }
    }
}