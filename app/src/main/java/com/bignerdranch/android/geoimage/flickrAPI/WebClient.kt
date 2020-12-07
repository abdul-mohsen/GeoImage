package com.bignerdranch.android.geoimage.flickrAPI

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.flickr.com"

class WebClient{

    companion object {
        val flickrAPI: FlickrAPI = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(FlickrInterceptor()).build())
            .build()
            .create(FlickrAPI::class.java)
    }
}