package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.adapter.AdapterStory
import com.faizurazadri.storyappsubmission1.adapter.LoadingStateAdapter
import com.faizurazadri.storyappsubmission1.data.source.model.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityListStoryBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.ui.viewmodel.ViewModelFactory
import com.faizurazadri.storyappsubmission1.utils.uriToFile
import com.google.gson.Gson

class ListStoryActivity : AppCompatActivity() {

    private lateinit var listStoryBinding: ActivityListStoryBinding
    private lateinit var userData: LoginResult
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val storyViewModel: StoryViewModel by viewModels {
        factory
    }

    private val launcherCreateStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
          getListStories()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listStoryBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(listStoryBinding.root)


        listStoryBinding.itemStory.layoutManager = LinearLayoutManager(this)


        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)


        userData =
            Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)


        getListStories()

        listStoryBinding.swipeStory.setOnRefreshListener {

            Handler(Looper.getMainLooper()).postDelayed({
                listStoryBinding.swipeStory.isRefreshing = false

                getListStories()
            }, 4000)
        }
    }

    private fun getListStories() {

        val adapterStory = AdapterStory()
        listStoryBinding.itemStory.adapter = adapterStory.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterStory.retry()
            })

        userData.token?.let { storyViewModel.getAllStories(it) }?.observe(this) {
            adapterStory.submitData(lifecycle, it)

        }


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
                launcherCreateStory.launch(Intent(this, AddStoryActivity::class.java))
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
}