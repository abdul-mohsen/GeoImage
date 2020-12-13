package com.bignerdranch.android.geoimage.flickrAPI

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

private const val API_KEY = "d14c694f6d02863f43e76e736616537e"

class FlickrInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val x = chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .addQueryParameter("format", "json")
                    .addQueryParameter("nojsoncallback", "1")
                    .addQueryParameter("extras", "url_s, url_o, views, owner_name, media")
                    .addQueryParameter("safesearch", "1")
                    .addEncodedQueryParameter("sort", "date-taken-asc")
                    .build()
            ).build()
        )
        Timber.d(x.request.url.toString())
        return x
    }
}
