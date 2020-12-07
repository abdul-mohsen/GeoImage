package com.bignerdranch.android.geoimage.control

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.geoimage.GeoLocation
import com.bignerdranch.android.geoimage.adapter.ImageAdapter
import com.bignerdranch.android.geoimage.R
import com.bignerdranch.android.geoimage.databinding.FragmentImageListBinding
import com.bignerdranch.android.geoimage.model.DeviceState
import com.bignerdranch.android.geoimage.viewmodel.ImageListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import android.location.Geocoder
import androidx.navigation.fragment.findNavController

class ImageList: Fragment() {
    private lateinit var binding: FragmentImageListBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageListViewModel: ImageListViewModel
    private lateinit var geoLocation: GeoLocation
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageListViewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)
        geoLocation = GeoLocation(LOCATION_PERMISSION_REQUEST_CODE) { location:Location ->
            val vLocation = imageListViewModel.location.value
            if (vLocation == null || (location.latitude != vLocation.latitude &&
                        location.longitude != vLocation.longitude)) {
                imageListViewModel.updateLocation(location)
                Timber.d("Got an update")
            } else {
                Timber.d("Got you")
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentImageListBinding.inflate(inflater, container, false)
        imageAdapter = ImageAdapter{ url: String, title: String ->
            findNavController().navigate(
                ImageListDirections.actionImageListToImagePreview(url, title)
            )
        }
        binding.grid.apply {
            this.adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        // observe the location change
        imageListViewModel.location.observe(viewLifecycleOwner, { location ->
            binding.textError.visibility = View.VISIBLE
            val geoAddress = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1)[0]
            if (imageListViewModel.deviceStateLiveData.value == DeviceState.Good){
                requireActivity().title = "${geoAddress.countryName}  ${geoAddress.featureName
                    .takeIf { it != "Unnamed Road" } ?: ""}"
                binding.textError.text = getString(R.string.loading)
                Timber.d("hmm  ${location.latitude} ${location.longitude} ")
                imageListViewModel.loadPhotos(location)
            }
        })

        // observe the imageList
        imageListViewModel.imageListLiveData.observe(viewLifecycleOwner, { list ->
            Timber.d( "should be once ${list.size}")
            if (list.isEmpty()) binding.textError.text = getString(R.string.no_image_found)
            else binding.textError.visibility = View.GONE
            imageAdapter.submitList(list)
        })
        Timber.d("Set up hmmm")


        // observe device state
        imageListViewModel.deviceStateLiveData.observe(viewLifecycleOwner, { deviceState ->
            Timber.d("State is updating")
            when(deviceState){
                DeviceState.NoGPS -> {
                    binding.textError.text = getString(R.string.no_gps_error)
                    binding.textError.visibility = View.VISIBLE
                    Timber.d( "No GPS signal")
                }
                DeviceState.NoInternet ->{
                    if (isInternetAvailable(requireContext()))
                        imageListViewModel.updateDeviceState(DeviceState.Good)
                    else {
                        binding.textError.text = getString(R.string.no_nternet_error)
                        binding.textError.visibility = View.VISIBLE
                        Timber.d( "No Internet")
                    }
                }
                DeviceState.Good ->{
                    geoLocation.requestUpdateLocation(requireContext())
                    Timber.d( "All good")
                }
                else -> Timber.d("The state is null")
            }
        })

        // refresh
        binding.swipeRefresh.setOnRefreshListener {
            geoLocation.requestUpdateLocation(requireContext())
            binding.swipeRefresh.isRefreshing = false
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() and (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Enable the my location layer if the permission has been granted.
                    Timber.d("enableMyLocation has been called")
                    geoLocation.enableMyLocation(requireActivity(), fusedLocationProviderClient)
                    { deviceState ->
                        if (deviceState != imageListViewModel.deviceStateLiveData.value)
                            imageListViewModel.updateDeviceState(deviceState)
                    }
                }
            }
            else -> return
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        if (imageListViewModel.deviceStateLiveData.value != DeviceState.Good){
            Timber.d("enableMyLocation has been called from onResume")
            geoLocation.enableMyLocation(requireActivity(), fusedLocationProviderClient) { deviceState ->
                Timber.d("enableMyLocation has been called from onResume X2")
                imageListViewModel.updateDeviceState(deviceState)
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}