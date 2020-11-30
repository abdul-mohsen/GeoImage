package com.bignerdranch.android.geoimage.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.geoimage.databinding.FragmentImageListBinding

class ImageList: Fragment() {
    private lateinit var binding: FragmentImageListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentImageListBinding.inflate(inflater, container,false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {

    }
}