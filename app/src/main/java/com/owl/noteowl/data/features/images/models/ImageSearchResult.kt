package com.owl.noteowl.data.features.images.models

import com.google.gson.annotations.SerializedName

class ImageSearchResult {
    @SerializedName("results")
    var images: List<Image> = emptyList()
}