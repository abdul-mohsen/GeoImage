package com.bignerdranch.android.geoimage.flickrAPI

import com.bignerdranch.android.geoimage.model.FlickrResponse
import com.bignerdranch.android.geoimage.model.Image
import com.bignerdranch.android.geoimage.model.ImageResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImageRepositoryTest {

    private lateinit var client: FlickrAPI
    private lateinit var imageRepository: ImageRepository

    @Before
    fun setUp() {
        client = mock()
        imageRepository = ImageRepository
    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun loadListOfImagesSuccessfully(): Unit = runBlocking {
        val image = Image(
            url = "https://live.staticflickr.com//__t.jpg"
        )
        val flowImageList = flow<List<Image>> { listOf(image) }
        val imageResponse = ImageResponse()
        val flickrResponse = FlickrResponse()
        imageResponse.photo = listOf(image)
        flickrResponse.photos = imageResponse

        client.stub {
            onBlocking { searchImages() } doReturn flickrResponse
        }

        ImageRepository.client = client
        imageRepository.dispatcher = TestCoroutineDispatcher()
        val flow = imageRepository.loadPhotos(0.0, 0.0)

        flow.collect { imageList ->
            imageList.first() shouldBeEqualTo image
        }
    }

    @Test
    fun loadListOfImagesOnNetworkException() {
    }
}
