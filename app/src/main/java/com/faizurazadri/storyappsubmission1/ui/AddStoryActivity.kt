package com.faizurazadri.storyappsubmission1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}