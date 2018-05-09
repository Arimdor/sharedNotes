package com.arimdor.sharednotes.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.repository.model.ResponseModel
import com.arimdor.sharednotes.repository.model.User
import com.arimdor.sharednotes.ui.book.BookActivity
import com.arimdor.sharednotes.ui.userRegister.RegisterActivity
import com.arimdor.sharednotes.utils.Constants
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var remember: Switch
    private lateinit var buttonLogin: Button
    private lateinit var linkSingUp: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()!!
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().setDateFormat("dd-MM-yyyy HH:mm:ss").create()))
            .build()
    private val sharedNotesAPI = retrofit.create(com.arimdor.sharednotes.repository.api.SharedNotesAPI::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.input_email)
        remember = findViewById(R.id.switchRemember)
        buttonLogin = findViewById(R.id.btn_login)
        linkSingUp = findViewById(R.id.link_signup)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        checkCredentialStored()

        buttonLogin.setOnClickListener { login(editTextEmail.text.toString()) }

        linkSingUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        if (!FirebaseInstanceId.getInstance().token.isNullOrBlank())
            Log.d("amd", FirebaseInstanceId.getInstance().token)

    }

    private fun checkCredentialStored(): Boolean {
        try {
            val email = sharedPreferences.getString("email", "")
            val rememberCheked = sharedPreferences.getBoolean("remember", false)

            if (!TextUtils.isEmpty(email) && rememberCheked) {
                editTextEmail.setText(email)
                remember.isChecked = rememberCheked
                return true
            }
            return false

        } catch (ex: NullPointerException) {
            Toast.makeText(this, "Ha ocurrido un error, porfavor intente nuevamente.", Toast.LENGTH_SHORT).show()
            return false
        }

    }

    private fun storePreference(email: String, token: String, saveCredentials: Boolean, loged: Boolean) {
        if (remember.isChecked) {
            val editor = sharedPreferences.edit()
            editor.putString("email", email)
            editor.putBoolean("remember", saveCredentials)
            editor.putString("token", token)
            editor.putBoolean("loged", loged)
            editor.apply()
        }
    }

    private fun login(email: String): Boolean {
        return if (email.length < 3) {
            Toast.makeText(this, "El nickname ingresado no es válido, porfavor vuelva a intentarlo", Toast.LENGTH_SHORT).show()
            false
        } else {
            val intent = Intent(this, BookActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val token = FirebaseInstanceId.getInstance().token
            if (!token.isNullOrBlank()) {
                val call = sharedNotesAPI.login(token!!, email)
                call.enqueue(object : Callback<ResponseModel<User>> {
                    override fun onFailure(call: Call<ResponseModel<User>>?, t: Throwable?) {
                        Log.d("test", "login fail ${t?.message}")
                    }

                    override fun onResponse(call: Call<ResponseModel<User>>?, response: Response<ResponseModel<User>>?) {
                        Log.d("test", "login ok ${response?.body()?.data}")
                        startActivity(intent)
                    }
                })
                storePreference(email, token, remember.isChecked, true)
                true
            } else {
                false
            }
        }
    }
}
