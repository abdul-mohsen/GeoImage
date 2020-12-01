package com.bignerdranch.android.geoimage.flickrAPI

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrAPI {

    @GET
    fun fetchUrlImages(@Url url:String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    suspend fun searchImages(@Query("text") query: String = "cat"): FlickrResponse
}