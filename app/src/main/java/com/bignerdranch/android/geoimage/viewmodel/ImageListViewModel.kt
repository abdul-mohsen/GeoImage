package com.bignerdranch.android.geoimage.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.bignerdranch.android.geoimage.flickrAPI.ImageRepository
import com.bignerdranch.android.geoimage.model.Image
import kotlinx.coroutines.launch

class ImageListViewModel: ViewModel() {
    private val repository:ImageRepository = ImageRepository()

    private val mutableImageListLiveData =  MutableLiveData<List<Image>>()

    fun loadPhotos(): LiveData<List<Image>> {
        viewModelScope.launch {
            val searchResponse = repository.client.searchImages()
            Log.d("test", searchResponse.photos.toString())
            val photosList = searchResponse.photos.photo.map { image ->
                Image(
                    id = image.id,
                    url = "https://live.staticflickr.com/${image.server}/${image.id}_${image.secret}.jpg",
                    title = image.title
                )
            }
            mutableImageListLiveData.postValue(photosList)
        }
        return mutableImageListLiveData
    }
}