package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.interfaces.ImageLoader
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.lang.Exception

object ImageRepository: ImageLoader {
     var client = WebClient.flickrAPI
     var dispatcher = Dispatchers.IO

     @FlowPreview
     override suspend fun loadPhotos(latitude: Double, longitude: Double, pageCount: Int)
     : Flow<List<Image>> = (1..pageCount).asFlow().flatMapMerge(concurrency = 4) { page ->
          flow {
               val searchResponse = client.searchImages(
                    lat = latitude,
                    lon = longitude,
                    page = page
               )
               val imageList = searchResponse.photos.photo.map { image ->
                    Image(id = image.id,
                         url = "https://live.staticflickr.com/${image.server}/${image.id}_" +
                                 "${image.secret}_t.jpg",
                         url_o = image.url_o,
                         title = image.title,
                         views = image.views,
                         description = image.description
                    )
               }

               emit(imageList)
          }
     }.retry(1) { e ->
          (e is Exception).also { if (it) delay(1000) }
     }.catch { e ->
          Timber.d(e.toString())
     }.flowOn(dispatcher)

}