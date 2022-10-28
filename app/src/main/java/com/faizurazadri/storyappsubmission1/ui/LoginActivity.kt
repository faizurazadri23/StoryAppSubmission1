package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityLoginBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.ui.viewmodel.ViewModelFactory
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {


    private lateinit var loginBinding: ActivityLoginBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }
    private var gson =  Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        checkLogin()

        storyViewModel.isLoading.observe(this) {
            loginBinding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        loginBinding.btnLogin.setOnClickListener {
            storyViewModel.login(
                loginBinding.edLoginEmail.text.toString(),
                loginBinding.edLoginPassword.text.toString()
            )

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        storyViewModel.isError.observe(this) {

            if (it) {
                Toast.makeText(this, applicationContext.getString(R.string.failed_login), Toast.LENGTH_LONG).show()
            }
        }

        storyViewModel.loginResponse.observe(this) {

            it.loginResult?.let { it1 -> saveUser(it1) }
            Intent(this, ListStoryActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }

        loginBinding.registerAccount.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also { startActivity(it) }
        }
    }

    private fun saveUser(loginResult: LoginResult) {
        gson = Gson()
        var jsonString = gson.toJson(loginResult)
        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("user", jsonString)
        editor.apply()
    }

    private fun checkLogin() {
        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)

        if (sharedPreference.contains("user")) {
            Intent(this, ListStoryActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }
    }


}