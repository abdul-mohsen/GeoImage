package com.bignerdranch.android.geoimage.interfaces

import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageLoader {
    suspend fun loadPhotos(latitude: Double, longitude: Double, pageCount: Int = 1): Flow<List<Image>>
}