package com.example.apprenticeassignment2.data


import com.google.gson.annotations.SerializedName

data class Photo(
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    @SerializedName("photographer_id")
    val photographerId: Int,
    @SerializedName("photographer_url")
    val photographerUrl: String,
    val src: Src,
    val url: String,
    val width: Int
)