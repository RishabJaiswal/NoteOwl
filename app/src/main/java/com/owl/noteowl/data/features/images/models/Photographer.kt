package com.owl.noteowl.data.features.images.models

import com.google.gson.annotations.SerializedName

class Photographer {
    @SerializedName("name")
    var name: String = ""

    @SerializedName("links")
    var links: Links? = null

    class Links {
        @SerializedName("html")
        var profilePagUrl: String = ""
    }
}