package com.bignerdranch.android.geoimage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.geoimage.R
import com.bignerdranch.android.geoimage.databinding.ImageItemBinding
import com.bignerdranch.android.geoimage.model.Image
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.lang.Exception

class ImageAdapter(val navigate: (String, String) -> Unit): ListAdapter<Image, ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {
    private lateinit var binding: ImageItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ImageItemBinding.inflate(layoutInflater,parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Timber.d( "$position  ${getItem(position).url}  ")
        holder.bind(getItem(position))
    }

    inner class ImageViewHolder(private val bindingHolder: ImageItemBinding)
        : RecyclerView.ViewHolder(bindingHolder.root){
        fun bind(image: Image){
            Timber.d( "___  ${image.id}")
            bindingHolder.imageViewHolder.setOnClickListener {
                navigate(image.url_o,image.id)
            }
            bindingHolder.positionText.text = image.views.toString()
            Picasso.get().load(image.url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_loading)
                .fit()
                .centerCrop()
                .into(bindingHolder.imageViewHolder, object : Callback {
                    override fun onSuccess() {
                        Timber.d("Loaded locally success")
                    }

                    override fun onError(e: Exception?) {
                        Timber.d("error")
                        Picasso.get().load(image.url)
                            .fit()
                            .centerCrop()
                            .into(bindingHolder.imageViewHolder)
                    }
                })
        }
    }
}