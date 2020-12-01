package com.bignerdranch.android.geoimage

import androidx.recyclerview.widget.DiffUtil
import com.bignerdranch.android.geoimage.model.Image

class ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean =
        oldItem == newItem

}