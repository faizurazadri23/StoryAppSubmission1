package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faizurazadri.storyappsubmission1.databinding.ActivityRegisterBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        storyViewModel.isLoading.observe(this) {
            registerBinding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        registerBinding.btnRegister.setOnClickListener {
            storyViewModel.createdAccount(
                registerBinding.edRegisterName.text.toString(),
                registerBinding.edRegisterEmail.text.toString(),
                registerBinding.edRegisterPassword.text.toString()
            )

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        storyViewModel.isError.observe(this) {

            if (it) {
                Toast.makeText(this, "Pendaftaran Gagal", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Buat Akun Berhasil", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


}