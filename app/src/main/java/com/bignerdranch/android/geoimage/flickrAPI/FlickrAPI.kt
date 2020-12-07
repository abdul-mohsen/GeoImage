package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.model.FlickrResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrAPI {

    @GET("services/rest?method=flickr.photos.search")
    suspend fun searchImages(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lon: Double = 0.0,
        @Query("radius") radius: Int = 15,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 1000

    ): FlickrResponse
}