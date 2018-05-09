package com.arimdor.sharednotes.utils

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.FragmentActivity


class InternetUtil {
    companion object {
        fun isOnline(context: FragmentActivity): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}