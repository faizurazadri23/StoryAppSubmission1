package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faizurazadri.storyappsubmission1.data.source.repository.ResultProcess
import com.faizurazadri.storyappsubmission1.databinding.ActivityRegisterBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        registerBinding.btnRegister.setOnClickListener {
            storyViewModel.createdAccount(
                registerBinding.edRegisterName.text.toString(),
                registerBinding.edRegisterEmail.text.toString(),
                registerBinding.edRegisterPassword.text.toString()
            ).observe(this) { result ->

                if (result != null) {
                    when (result) {
                        is ResultProcess.Loading -> {
                            registerBinding.loading.visibility = View.VISIBLE
                        }
                        is ResultProcess.Success -> {

                            finish()
                            Toast.makeText(this, result.data.message, Toast.LENGTH_LONG).show()
                        }

                        is ResultProcess.Error -> {
                            registerBinding.loading.visibility = View.GONE
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }


}