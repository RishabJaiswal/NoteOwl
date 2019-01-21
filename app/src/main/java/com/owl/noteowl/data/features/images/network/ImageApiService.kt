package com.owl.noteowl.data.features.images.network

import com.owl.noteowl.data.features.images.models.Image
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApiService {

    @GET("photos/")
    fun getImages(
        @Query("client_id") clientId: String,
        @Query("per_page") pageSize: Int,
        @Query("page") pageNo: Int
    ): Call<List<Image>>
}