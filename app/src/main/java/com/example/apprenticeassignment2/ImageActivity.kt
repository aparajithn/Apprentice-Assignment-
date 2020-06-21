package com.example.apprenticeassignment2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    companion object {
        fun start(activity: Activity, imageUrl: String) {
            val intent = Intent(activity, ImageActivity::class.java)
            intent.putExtra("imageUrl", imageUrl)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUrl = intent.extras?.getString("imageUrl")
        Log.e("ImageActivity", imageUrl)
        Glide.with(this)
            .load(imageUrl)
            .into(full_image)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}