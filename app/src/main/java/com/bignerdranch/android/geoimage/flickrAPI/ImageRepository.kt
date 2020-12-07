package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.model.Image
import timber.log.Timber

class ImageRepository {
     private val client = WebClient.flickrAPI

     suspend fun getSearch(latitude: Double, longitude: Double):List<Image> {
          val searchResponse = client.searchImages(
               lat = latitude,
               lon = longitude
          )

          return searchResponse.photos.photo.map { image ->
//               Timber.d(image.url_o)
               Image(id = image.id,
                    url = "https://live.staticflickr.com/${image.server}/${image.id}_" +
                            "${image.secret}_t.jpg",
                    url_o = image.url_o,
                    title = image.title,
                    views = image.views,
                    description = image.description
               )
          }
     }
}