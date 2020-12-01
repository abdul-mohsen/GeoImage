package com.bignerdranch.android.geoimage.flickrAPI

class ImageRepository {
     var client = WebClient.flickrAPI

     suspend fun searchImages() =  client.searchImages()
}