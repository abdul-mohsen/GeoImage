package com.bignerdranch.android.geoimage.control

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.geoimage.ImageAdapter
import com.bignerdranch.android.geoimage.databinding.FragmentImageListBinding
import com.bignerdranch.android.geoimage.model.Image
import com.bignerdranch.android.geoimage.viewmodel.ImageListViewModel
import com.squareup.picasso.Picasso

class ImageList: Fragment() {
    private lateinit var binding: FragmentImageListBinding
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Picasso.get().setIndicatorsEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentImageListBinding.inflate(inflater, container,false)
        val imageListViewModel: ImageListViewModel by viewModels()
        imageAdapter = ImageAdapter()
        binding.grid.apply {
            this.adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        imageListViewModel.loadPhotos().observe(viewLifecycleOwner, { list ->
            Log.d("test", "should be once ${list.size}")
            imageAdapter.submitList(list)
        })


        setupUI()
        return binding.root
    }

    private fun setupUI() {

    }
}