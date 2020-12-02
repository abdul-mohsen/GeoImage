package com.bignerdranch.android.geoimage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.geoimage.databinding.ImageItemBinding
import com.bignerdranch.android.geoimage.model.Image
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImageAdapter: ListAdapter<Image,ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {
    private lateinit var binding: ImageItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ImageItemBinding.inflate(layoutInflater,parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Log.d("test number", "$position ${holder.position} ${getItem(position).url}  ")
        holder.bind(getItem(position), position)
    }

    inner class ImageViewHolder(private val bindingHolder: ImageItemBinding): RecyclerView.ViewHolder(bindingHolder.root){
        fun bind(image: Image, position: Int){
            Log.d("test binding", "___  ${image.id}")
            bindingHolder.positionText.text = image.views.toString()
            Picasso.get().load(image.url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .into(bindingHolder.imageViewHolder, object : Callback {
                    override fun onSuccess() {
                        Log.d("test","success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d("test","error")
                        Picasso.get().load(image.url)
                            .resize(400, 400)
                            .centerCrop()
                            .into(bindingHolder.imageViewHolder)
                    }

                })
        }
    }
}