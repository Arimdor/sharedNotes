package com.arimdor.sharednotes.ui.userRegister

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arimdor.sharednotes.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setDefaultFragment()
    }

    private fun setDefaultFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_register, RegisterFragment())
                .commit()
    }
}
