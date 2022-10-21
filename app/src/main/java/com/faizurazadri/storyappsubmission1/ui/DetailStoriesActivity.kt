package com.faizurazadri.storyappsubmission1.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.databinding.ActivityDetailStoriesBinding

class DetailStoriesActivity : AppCompatActivity() {

    private lateinit var detailStoriesBinding: ActivityDetailStoriesBinding

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoriesBinding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(detailStoriesBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras

        if (extras != null) {
            detailStoriesBinding.tvDetailName.text = extras.getString(EXTRA_NAME)
            detailStoriesBinding.tvDetailDescription.text = extras.getString(EXTRA_DESCRIPTION)
            Glide.with(applicationContext)
                .load(extras.getString(EXTRA_IMAGE))
                .apply(RequestOptions.placeholderOf(R.drawable.no_image).error(R.drawable.no_image))
                .into(detailStoriesBinding.ivDetailPhoto)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}