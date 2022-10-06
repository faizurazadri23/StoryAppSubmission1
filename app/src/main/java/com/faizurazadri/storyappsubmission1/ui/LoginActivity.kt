package com.faizurazadri.storyappsubmission1.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.faizurazadri.storyappsubmission1.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        loginBinding.registerAccount.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also { startActivity(it) }
        }
    }


}