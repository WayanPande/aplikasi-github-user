package com.example.aplikasigithubuser.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class IsUserConnectedToInternet {
     fun check(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)  || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
        return false

    }

}