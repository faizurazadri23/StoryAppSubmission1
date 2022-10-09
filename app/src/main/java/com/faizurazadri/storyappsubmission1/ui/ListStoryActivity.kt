package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.adapter.AdapterStory
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityListStoryBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.google.gson.Gson

class ListStoryActivity : AppCompatActivity() {

    private lateinit var listStoryBinding: ActivityListStoryBinding;
    private val storyViewModel: StoryViewModel by viewModels()
    private val adapterStory = AdapterStory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStoryBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(listStoryBinding.root)

        storyViewModel.isLoading.observe(this) {
            listStoryBinding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)


        val userData = Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)

        userData.token?.let { storyViewModel.getAllStories(it) }

        getListStories()
    }

    private fun getListStories() {
        listStoryBinding.loading.visibility = View.VISIBLE
        storyViewModel.storyList.observe(this) {
            listStoryBinding.emptyData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

            adapterStory.setStories(it)
            adapterStory.notifyDataSetChanged()
            listStoryBinding.loading.visibility = View.GONE
        }

        with(listStoryBinding.itemStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterStory
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit_application -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}