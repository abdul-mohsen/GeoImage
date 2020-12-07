package com.bignerdranch.android.geoimage.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.DeviceState
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ImageListViewModel: ViewModel() {
    private val repository: ImageRepository = ImageRepository

    private val _imageListLiveData = MutableLiveData<List<Image>>()
    val imageListLiveData: LiveData<List<Image>>
        get() = _imageListLiveData

    private val _location: MutableStateFlow<Location> = MutableStateFlow(Location("empty"))
    val location: StateFlow<Location> = _location

    private val _deviceStateLiveData: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.NoGPS)
    val deviceState: StateFlow<DeviceState> = _deviceStateLiveData

    @FlowPreview
    fun loadPhotos(location: Location) {
        viewModelScope.launch {
            val photosList: MutableList<Image> = mutableListOf()
            repository.loadPhotos(
                latitude = location.latitude,
                longitude = location.longitude,
                pageCount = 4).collect { value ->
                photosList.addAll(value)
            }
            _imageListLiveData.postValue(photosList.sortedByDescending { it.views })

        }
    }

    fun updateLocation(cLocation: Location) {
        _location.value = cLocation
    }

    fun updateDeviceState(deviceState: DeviceState) {
        _deviceStateLiveData.value = deviceState
    }
}