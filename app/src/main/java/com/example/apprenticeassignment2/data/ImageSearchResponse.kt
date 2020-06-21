package com.example.apprenticeassignment2.data


import com.google.gson.annotations.SerializedName

data class ImageSearchResponse(
    @SerializedName("next_page")
    val nextPage: String,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val photos: MutableList<Photo>,
    @SerializedName("total_results")
    val totalResults: Int
)