package com.sospos.garson

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sospos.garson.api.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)
        
        btnLogin.setOnClickListener {
            login()
        }
    }
    
    private fun login() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        if (username.isEmpty() || password.isEmpty()) {
            tvError.text = getString(R.string.login_error)
            tvError.visibility = View.VISIBLE
            return
        }
        
        tvError.visibility = View.GONE
        btnLogin.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val credentials = mapOf(
                    "username" to username,
                    "password" to password
                )
                
                val response = RetrofitClient.apiService.login(credentials)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val user = response.body()?.data
                    
                    // Save user data (in production, use SharedPreferences or DataStore)
                    val sharedPref = getSharedPreferences("SOSPOS", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putInt("user_id", user?.id ?: 0)
                        putString("username", user?.username)
                        putString("token", user?.token)
                        apply()
                    }
                    
                    // Navigate to MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    tvError.text = getString(R.string.login_error)
                    tvError.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                tvError.text = getString(R.string.error_network)
                tvError.visibility = View.VISIBLE
                e.printStackTrace()
            } finally {
                btnLogin.isEnabled = true
            }
        }
    }
}
