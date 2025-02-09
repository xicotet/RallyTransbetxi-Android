package com.canolabs.rallytransbetxi.ui.miscellaneous.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
class NetworkCheckerImpl(
    private val context: Context
) : NetworkChecker {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}