package com.owl.noteowl.data.features.images.models

import com.google.gson.annotations.SerializedName

class Image {
    @SerializedName("urls")
    var urls: ImageUrls? = null

    fun getDisplayImageUrl(): String? {
        return urls?.regular
    }
}