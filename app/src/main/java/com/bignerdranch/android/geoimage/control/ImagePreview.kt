package com.bignerdranch.android.geoimage.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.geoimage.R
import com.bignerdranch.android.geoimage.databinding.FragmentPreviewBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber

class ImagePreview: Fragment() {
    private lateinit var binding: FragmentPreviewBinding
    private val args: ImagePreviewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.d(args.url)
        binding = FragmentPreviewBinding.inflate(inflater, container, false)

        binding.title.text = args.title

        Picasso.get()
            .load(args.url)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
            .placeholder(R.drawable.ic_loading)
            .fit()
            .centerInside()
            .into(binding.image)

        return binding.root
    }
}