package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.adapter.AdapterStory
import com.faizurazadri.storyappsubmission1.adapter.LoadingStateAdapter
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityListStoryBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.ui.viewmodel.ViewModelFactory
import com.google.gson.Gson

class ListStoryActivity : AppCompatActivity() {

    private lateinit var listStoryBinding: ActivityListStoryBinding
    private lateinit var userData: LoginResult
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }
    private val adapterStory = AdapterStory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStoryBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(listStoryBinding.root)

        /*storyViewModel.isLoading.observe(this) {
            listStoryBinding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }*/

        listStoryBinding.itemStory.layoutManager = LinearLayoutManager(this)


        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)


        userData =
            Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)


        getListStories()
    }

    private fun getListStories() {
        /* listStoryBinding.loading.visibility = View.VISIBLE
         storyViewModel.storyList.observe(this) {
             listStoryBinding.emptyData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE

             adapterStory.differ.submitList(it)

             listStoryBinding.apply {
                 itemStory.apply {
                     layoutManager = LinearLayoutManager(this@ListStoryActivity)
                     adapter = adapterStory
                 }
             }

             listStoryBinding.loading.visibility = View.GONE
         }*/

        val adapterStory = AdapterStory()
        listStoryBinding.itemStory.adapter = adapterStory.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterStory.retry()
            })

        userData.token?.let { storyViewModel.getAllStories(it) }?.observe(this) {
            adapterStory.submitData(lifecycle, it)

        }

        /*storyViewModel.getAllStories.observe(this) {
            adapterStory.submitData(lifecycle, it)
        }*/


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showAlertDialogLogout()
                true
            }

            R.id.add_story -> {
                Intent(this, AddStoryActivity::class.java).also { startActivity(it) }
                true
            }

            R.id.action_map -> {
                Intent(this, MapsActivity::class.java).also { startActivity(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showAlertDialogLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(applicationContext.getString(R.string.keluar))
        builder.setMessage(applicationContext.getString(R.string.description_alert_logout))
        builder.setPositiveButton(android.R.string.yes) { _, _ ->

            logout()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun logout() {
        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove("user")
        editor.clear()
        editor.apply()


        Intent(this, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

    }
}