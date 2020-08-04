package kz.evilteamgenius.firstapp.activity


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.evilteamgenius.firstapp.R
import kz.evilteamgenius.firstapp.utils.NetworkReceiver
import kz.evilteamgenius.firstapp.utils.enablePolicy
import kz.evilteamgenius.firstapp.viewModel.MainViewModel
import timber.log.Timber


class MainActivity : FragmentActivity()
    , AmbientModeSupport.AmbientCallbackProvider, NetworkReceiver.ConnectivityReceiverListener {
    private lateinit var ambientController: AmbientModeSupport.AmbientController
    private lateinit var viewModel: MainViewModel

    // The BroadcastReceiver that tracks network connectivity changes.
    private var receiver: NetworkReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        ambientController = AmbientModeSupport.attach(this)
        enablePolicy()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        receiver = NetworkReceiver()
        receiver?.setListener(this)
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        GlobalScope.launch {
            viewModel.loadAllData()
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()

    inner class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

        private var navController = findNavController(R.id.fragments)

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
            val navDestination = navController.currentDestination
            Log.d("activity", "onUpdateAmbient: ${navDestination.toString()}")
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Toast.makeText(this, "internet ->$isConnected", Toast.LENGTH_SHORT).show()
        println("kuka internet -> $isConnected")
        viewModel.isInternetActive.value = isConnected
    }

    private fun startCheckInternet() {
        if (receiver != null) {
            Timber.d("Receiver Can't register receiver which already has been registered")
        } else {
            try {
                receiver = NetworkReceiver()
                registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
                receiver?.setListener(this)
            } catch (err: Exception) {
                Timber.e("%s --> %s", err.javaClass.name, err.message)
            }
        }
    }

    private fun stopCheckInternet() {
        try {
            if (receiver == null) {
                Timber.d("Receiver Can't unregister a receiver which was never registered")
            } else {
                receiver?.abortBroadcast()
                unregisterReceiver(receiver)
                receiver = null
            }
        } catch (err: java.lang.Exception) {
            Timber.e("%s --> %s", err.javaClass.name, err.message)
            Timber.e("Receiver not registerer Couldn't get context")
        }
    }

    override fun onStart() {
        super.onStart()
        startCheckInternet()
    }

    override fun onStop() {
        super.onStop()
        stopCheckInternet()
    }
}