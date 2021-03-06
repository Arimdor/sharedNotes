package com.arimdor.sharednotes.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.arimdor.sharednotes.ui.book.BookActivity
import com.arimdor.sharednotes.ui.login.LoginActivity
import com.google.firebase.iid.FirebaseInstanceId


class SplashActivity : AppCompatActivity() {

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, BookActivity::class.java)
        SystemClock.sleep(200)

        if (!FirebaseInstanceId.getInstance().token.isNullOrBlank()) {
            Log.d("amd", FirebaseInstanceId.getInstance().token)
        }

        if (!TextUtils.isEmpty(sharedPreferences!!.getString("email", "")) && !TextUtils.isEmpty(sharedPreferences!!.getString("password", ""))
                && sharedPreferences!!.getBoolean("loged", false)) {
            startActivity(intentMain)
        } else {
            startActivity(intentLogin)
        }

        finish()

    }
}
