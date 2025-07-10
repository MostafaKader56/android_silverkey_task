package com.bb4first.bluebuddy.network

import com.silverkey.task.utils.Constants

object RequestHeaders {
    fun getHeaders(): Map<String, String> {
        val map = HashMap<String, String>()
        map[Constants.PLATFORM] = "Android"
        return map
    }
}
