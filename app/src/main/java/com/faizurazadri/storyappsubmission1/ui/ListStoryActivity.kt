package com.faizurazadri.storyappsubmission1.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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


        val userData =
            Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)

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
            R.id.action_logout -> {
                showAlertDialogLogout()
                true
            }

            R.id.add_story -> {
                Intent(this, AddStoryActivity::class.java).also { startActivity(it) }
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