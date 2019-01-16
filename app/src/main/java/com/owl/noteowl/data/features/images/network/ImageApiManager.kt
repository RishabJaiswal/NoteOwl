package com.owl.noteowl.data.features.images.network

import com.owl.noteowl.data.NoteOwlRetrofit
import com.owl.noteowl.data.features.images.models.Image
import retrofit2.Call

class ImageApiManager {
    val baseUrl = "https://api.unsplash.com/photos"
    val clientId = "066dcd18f78faa93ac74ec0ca034aff957d17e7768cacc50d4e04f2596ec453b"

    val imageApiService by lazy {
        NoteOwlRetrofit.createService(ImageApiService::class.java)
    }

    fun getImages(pageSize: Int, pageNo: Int): Call<List<Image>> {
        return imageApiService.getImages(baseUrl, clientId, pageSize, pageNo)
    }
}