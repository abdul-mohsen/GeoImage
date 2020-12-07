package com.bignerdranch.android.geoimage.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.DeviceState
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.launch
import timber.log.Timber

class ImageListViewModel: ViewModel() {
    private val repository: ImageRepository = ImageRepository()

    private val _imageListLiveData = MutableLiveData<List<Image>>()
    val imageListLiveData: LiveData<List<Image>>
        get() = _imageListLiveData

    private var _location: MutableLiveData<Location> = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    private var _deviceStateLiveData: MutableLiveData<DeviceState> = MutableLiveData<DeviceState>()
    val deviceStateLiveData
        get() = _deviceStateLiveData

    fun loadPhotos(location: Location) {
        viewModelScope.launch {
            try {
                val photosList = repository.getSearch(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                _imageListLiveData.postValue(photosList.sortedByDescending { it.views })
            } catch (e: Exception) {
                Timber.d("boom error")
            }
        }
    }

    fun updateLocation(cLocation: Location) {
        _location.postValue(cLocation)
        Timber.d("I got an update")
    }

    fun updateDeviceState(deviceState: DeviceState) {
        _deviceStateLiveData.postValue(deviceState)
    }
}