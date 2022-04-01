package com.jagl.globalsight.data.network

import com.jagl.globalsight.data.model.ResponseYelpApi
import com.jagl.globalsight.util.Constants.API_KEY
import com.jagl.globalsight.util.Constants.URL
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface YelpApiClient {

    @Headers("Authorization: Bearer $API_KEY")
    @GET("businesses/search")
    suspend fun search(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("term") term: String
    ) : ResponseYelpApi

    @Headers("Authorization: Bearer $API_KEY")
    @GET("autocomplete")
    suspend fun autocomplete(
        @Query("text") text: String
    ) : ResponseBody

    companion object{
        fun createService() = YelpService(
            Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpApiClient::class.java)
        )

    }
}