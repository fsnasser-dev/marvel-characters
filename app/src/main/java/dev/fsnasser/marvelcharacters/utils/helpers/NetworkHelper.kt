package dev.fsnasser.marvelcharacters.utils.helpers

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHelper @Inject constructor(private val mApplicationContext: Context) {

    val isConnectedToInternet: Boolean
        get() {
            val connectionManager = mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

}