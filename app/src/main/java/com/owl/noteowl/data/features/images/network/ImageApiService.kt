package com.owl.noteowl.data.features.images.network

import com.owl.noteowl.data.features.images.models.Image
import com.owl.noteowl.data.features.images.models.ImageSearchResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImageApiService {

    @GET("photos/")
    fun getImages(
        @Query("client_id") clientId: String,
        @Query("per_page") pageSize: Int,
        @Query("page") pageNo: Int
    ): Call<List<Image>>

    //searching images
    @GET("search/photos/")
    fun searchImages(
        @Query("client_id") clientId: String,
        @Query("query") searchQuery: String,
        @Query("per_page") pageSize: Int,
        @Query("page") pageNo: Int
    ): Call<ImageSearchResult>

    @GET("{download_url}/")
    fun incrementDownload(
        @Path("download_url") downloadUrl: String,
        @Query("client_id") clientId: String
    ): Call<ResponseBody>
}