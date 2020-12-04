package com.bignerdranch.android.geoimage.flickrAPI

import androidx.paging.PagingSource
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.Image
import retrofit2.HttpException
import java.io.IOException

class ImagePagingSource(
    private val imageRepository: ImageRepository,
    private val lat: Double,
    private val lon: Double

): PagingSource<Int, Image>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val nextPageNumber = params.key ?: 1
        try {
            val searchResponse = imageRepository.client.searchImages(
                lat = lat,
                lon = lon,
                page = nextPageNumber)
            return LoadResult.Page(
                data = searchResponse.photos.photo,
                prevKey = null,
                nextKey = searchResponse.photos.page + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

}