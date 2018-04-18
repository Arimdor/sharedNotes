package com.arimdor.sharednotes.ui.userRegister

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
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
