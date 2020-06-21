package com.example.apprenticeassignment2.services

import com.example.apprenticeassignment2.data.ImageSearchResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val API_KEY = "563492ad6f91700001000001705f33599654497aac9a3722eb30fded"

//Perform get request from API

interface PexelApiService {
    @GET("search")
    @Headers("Authorization:$API_KEY")
    fun getImage(
        @Query("page") nextPage: Int,
        @Query("query") input: String,
        @Query("per_page") itemsPerPage: Int
    ): Deferred<Response<ImageSearchResponse>>


    companion object {
        private const val BASE_URL = "https://api.pexels.com/v1/"
        fun getApi(): PexelApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(PexelApiService::class.java)
        }
    }
}