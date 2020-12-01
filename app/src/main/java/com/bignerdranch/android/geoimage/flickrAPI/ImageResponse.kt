package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.model.Image
import com.google.gson.annotations.SerializedName

class ImageResponse {
    @SerializedName("photo")
    lateinit var photo: List<Image>
}
