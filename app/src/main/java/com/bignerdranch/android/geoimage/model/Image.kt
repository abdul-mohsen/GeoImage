package com.bignerdranch.android.geoimage.model

import com.google.gson.annotations.SerializedName

data class Image(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = "",
    var secret: String = "",
    var server: String = "",
    var views: Int = 0,
    var description: String = "",
    var url_o: String = ""
)