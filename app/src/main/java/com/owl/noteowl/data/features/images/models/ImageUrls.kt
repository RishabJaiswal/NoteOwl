package com.owl.noteowl.data.features.images.models

import com.google.gson.annotations.SerializedName

class ImageUrls {
    @SerializedName("regular")
    var regular: String? = null

    @SerializedName("thumb")
    var thumb: String? = null
}