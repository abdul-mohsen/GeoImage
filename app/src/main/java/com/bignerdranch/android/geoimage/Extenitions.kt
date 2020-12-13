package com.bignerdranch.android.geoimage

import com.bignerdranch.android.geoimage.model.FlickrResponse
import com.bignerdranch.android.geoimage.model.Image

fun FlickrResponse.images(): List<Image> = this.photos.photo
