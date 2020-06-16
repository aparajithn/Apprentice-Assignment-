package com.example.apprenticeassignment2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apprenticeassignment2.data.ImageSearchResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var datList = ArrayList<ImageSearchResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch(Dispatchers.Main) {
            Log.e("MainActivity", "Im here")
           val postsRequest = PexelApiService.getApi().getImage("Nature",2)
            val postsResponse = postsRequest.await()
            if (postsResponse.isSuccessful) {
                textview.text = postsResponse.body().toString()
            } else {
                Log.e("MainActivity", "Error ${postsResponse.code()}")
            }
        }


    }



}
