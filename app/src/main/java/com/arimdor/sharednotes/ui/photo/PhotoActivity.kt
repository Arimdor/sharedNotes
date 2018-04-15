package com.arimdor.sharednotes.ui.photo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arimdor.sharednotes.R

class PhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_photo, PhotoFragment())
                .commit()
    }

}
