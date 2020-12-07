package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.`interface`.ImageLoader
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow

object ImageRepository: ImageLoader {
     private val client = WebClient.flickrAPI

     @FlowPreview
     override suspend fun loadPhotos(latitude: Double, longitude: Double, pageCount: Int = 1)
     : Flow<List<Image>> = (1..pageCount).asFlow().flatMapMerge(concurrency = 4) { page ->
          flow {
               emit(
                    client.searchImages(
                         lat = latitude,
                         lon = longitude,
                         page = page
                    ).photos.photo.map { image ->
                         Image(id = image.id,
                              url = "https://live.staticflickr.com/${image.server}/${image.id}_" +
                                      "${image.secret}_t.jpg",
                              url_o = image.url_o,
                              title = image.title,
                              views = image.views,
                              description = image.description
                         )
                    }
               )
          }
     }

}