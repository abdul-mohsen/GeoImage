package com.bignerdranch.android.geoimage.viewmodel

import android.location.Address
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.DeviceState
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class ImageListViewModel : ViewModel() {
    private val repository: ImageRepository = ImageRepository

    private val _imageList = MutableStateFlow<List<Image>>(emptyList())
    val imageList: StateFlow<List<Image>> = _imageList

    private val _location: MutableStateFlow<Location> = MutableStateFlow(Location("empty"))
    val location: StateFlow<Location> = _location

    private val _deviceState: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.NoGPS)
    val deviceState: StateFlow<DeviceState> = _deviceState

    private val _geoAddress: MutableStateFlow<Address> = MutableStateFlow(Address(Locale("Unknown")))
    val geoAddress: StateFlow<Address> = _geoAddress

    @FlowPreview
    suspend fun loadPhotos(location: Location) {
        viewModelScope.launch {
            val photosList: MutableList<Image> = mutableListOf()
            repository.loadPhotos(
                latitude = location.latitude,
                longitude = location.longitude,
                pageCount = 4
            ).collect { value ->
                photosList.addAll(value)
            }
            Timber.d("why are not you updating")
            _imageList.emit(photosList.sortedByDescending { it.views })
        }
    }

    fun updateLocation(cLocation: Location) {
        Timber.d("the location has been updated with ${cLocation.latitude}, ${cLocation.longitude}")
        _location.value = cLocation
    }

    suspend fun updateDeviceState(deviceState: DeviceState) {
        Timber.d("the state has been updated with $deviceState")
        _deviceState.emit(deviceState)
    }

    fun currentState(): DeviceState = deviceState.value

    fun updateGeoAddress(address: Address) {
        _geoAddress.value = address
    }
}
