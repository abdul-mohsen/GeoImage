package com.bignerdranch.android.geoimage.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.geoimage.databinding.FragmentPreviewBinding
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


        Picasso.get()
                .load(args.url)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .fit()
                .centerInside()
                .into(binding.image)

        return binding.root
    }

}