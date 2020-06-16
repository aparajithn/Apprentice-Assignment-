package com.example.apprenticeassignment2

import com.example.apprenticeassignment2.data.ImageSearchResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val API_KEY = "563492ad6f91700001000001705f33599654497aac9a3722eb30fded"

interface PexelApiService {
    @GET("search")
    @Headers("Authorization:$API_KEY")
    fun getImage(
        @Query("query") input: String,
        @Query("per_page") itemsPerPage: Int
    ): Deferred<Response<List<ImageSearchResponse>>>

    companion object {
        private const val BASE_URL = "https://api.pexels.com/v1/"
        fun getApi(): PexelApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(PexelApiService::class.java)
    }
}