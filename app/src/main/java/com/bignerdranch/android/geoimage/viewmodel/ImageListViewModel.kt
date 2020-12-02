package com.bignerdranch.android.geoimage.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.launch

class ImageListViewModel: ViewModel() {
    private val repository:ImageRepository = ImageRepository()
    private val _imageListLiveData =  MutableLiveData<List<Image>>()
    val imageListLiveData: LiveData<List<Image>>
        get() =  _imageListLiveData
    private var _location: MutableLiveData<Location> = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    fun loadPhotos(location: Location) {
        viewModelScope.launch {
            try {
                val searchResponse = repository.client.searchImages(
                    lat = location.latitude.toString(),
                    lon = location.longitude.toString()
                )
                val photosList = searchResponse.photos.photo.map { image ->
                    Image(
                        id = image.id,
                        url = "https://live.staticflickr.com/${image.server}/${image.id}_${image.secret}.jpg",
                        title = image.title,
                        views = image.views,
                        description = image.description
                    )
                }
                _imageListLiveData.postValue(photosList.sortedByDescending { it.views })
            } catch (e: Exception){
                Log.d("hmmm","boom error")
            }


        }
    }

    fun updateLocation(cLocation: Location){
        _location.postValue(cLocation)
    }
}