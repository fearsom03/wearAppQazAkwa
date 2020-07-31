package kz.evilteamgenius.firstapp.activity


import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.wear.ambient.AmbientModeSupport
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.utils.enablePolicy
import kz.evilteamgenius.firstapp.viewModel.MainViewModel


class MainActivity : FragmentActivity()
    , AmbientModeSupport.AmbientCallbackProvider {
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ambientController = AmbientModeSupport.attach(this)
        enablePolicy()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.loadAllData()
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()

    inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

        var navController = findNavController(R.id.fragments)

        override fun onEnterAmbient(ambientDetails: Bundle?) {
            // Handle entering ambient mode

            // If the current destination is the FirstFragment, it won't navigate.
            val currentDest = navController.currentDestination
            if (currentDest?.label != "fragment_blank") {
                navController.navigate(R.id.blankFragment)
            }
        }

        override fun onExitAmbient() {
            // Handle exiting ambient mode
            val currentDest = navController.currentDestination
            // If the current destination is the FirstFragment, it won't navigate.

            if (currentDest?.label != "fragment_blank") {
                navController.popBackStack()
            }
        }

        override fun onUpdateAmbient() {
            // Update the content
            var kjsfh = navController.currentDestination
        }
    }
}