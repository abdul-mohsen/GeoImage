package com.bignerdranch.android.geoimage.model

import com.bignerdranch.android.geoimage.model.Image
import com.google.gson.annotations.SerializedName

class ImageResponse {
    @SerializedName("photo")
    lateinit var photo: List<Image>
    @SerializedName("page")
    var page: Int = 0

}
