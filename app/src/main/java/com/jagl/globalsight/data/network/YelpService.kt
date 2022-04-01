package com.jagl.globalsight.data.network

import android.util.Log
import com.jagl.globalsight.data.model.ResponseYelpApi

class YelpService(
    private val api: YelpApiClient
) {
    private val TAG = YelpService::class.java.simpleName

    suspend fun getSearch(latitude: Double, longitude: Double, term: String): ResponseYelpApi? {
        return try {
            Log.d(TAG, "Make querry")
            val result = api.search(latitude, longitude, term)
            Log.d(TAG, "End of the service")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Exception\n${e}")
            null
        }

    }
}
