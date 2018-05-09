package com.arimdor.sharednotes.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.arimdor.sharednotes.R
import com.arimdor.sharednotes.ui.book.BookActivity
import com.arimdor.sharednotes.ui.userRegister.RegisterActivity
import com.google.firebase.iid.FirebaseInstanceId

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var remember: Switch
    private lateinit var buttonLogin: Button
    private lateinit var linkSingUp: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.input_email)
        editTextPassword = findViewById(R.id.input_password)
        remember = findViewById(R.id.switchRemember)
        buttonLogin = findViewById(R.id.btn_login)
        linkSingUp = findViewById(R.id.link_signup)

        sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        checkCredentialStored()

        buttonLogin.setOnClickListener { login(editTextEmail.text.toString(), editTextPassword.text.toString()) }

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
            val password = sharedPreferences.getString("password", "")
            val rememberCheked = sharedPreferences.getBoolean("remember", false)

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && rememberCheked) {
                editTextEmail.setText(email)
                editTextPassword.setText(password)
                remember.isChecked = rememberCheked
                return true
            }
            return false

        } catch (ex: NullPointerException) {
            Toast.makeText(this, "Ha ocurrido un error, porfavor intente nuevamente.", Toast.LENGTH_SHORT).show()
            return false
        }

    }

    private fun storePreference(email: String, password: String, saveCredentials: Boolean, loged: Boolean) {
        if (remember.isChecked) {
            val editor = sharedPreferences.edit()
            editor.putString("email", email)
            editor.putString("password", password)
            editor.putBoolean("remember", saveCredentials)
            editor.putBoolean("loged", loged)
            editor.apply()
        }
    }

    private fun login(email: String, password: String): Boolean {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El email ingresado no es valido, porfavor vuelva a intentarlo", Toast.LENGTH_SHORT).show()
            false
        } else if (password.length < 4) {
            Toast.makeText(this, "La password ingresado no es valida, porfavor vuelva a intentarlo.", Toast.LENGTH_SHORT).show()
            true
        } else {
            val intent = Intent(this, BookActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            storePreference(email, password, remember.isChecked, true)
            startActivity(intent)
            true
        }
    }
}
