package com.faizurazadri.storyappsubmission1.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResult
import com.faizurazadri.storyappsubmission1.databinding.ActivityAddStoryBinding
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.utils.createCustomTempFile
import com.faizurazadri.storyappsubmission1.utils.uriToFile
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    private val storyViewModel: StoryViewModel by viewModels()

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this, "Tidak memiliki hak akses", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private lateinit var userData: LoginResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = applicationContext.getString(R.string.tambah_story)

        storyViewModel.isLoading.observe(this) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        val sharedPreference = getSharedPreferences("auth", Context.MODE_PRIVATE)


        userData =
            Gson().fromJson(sharedPreference.getString("user", ""), LoginResult::class.java)



        binding.buttonCamera.setOnClickListener({ startTakePhoto() })
        binding.buttonGallery.setOnClickListener({ startGallery() })
        binding.buttonAdd.setOnClickListener(View.OnClickListener {

            uploadImage()

            storyViewModel.isError.observe(this) {

                if (it) {
                    Toast.makeText(
                        this,
                        applicationContext.getString(R.string.failed_login),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            storyViewModel.message.observe(this) {
                finish()
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }


        })
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivItemPhoto.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        if (getFile != null) {
            val file = getFile as File

            val description =
                binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            userData.token?.let { storyViewModel.addNewStories(it, imageMultipart, description) }

        } else {
            Toast.makeText(
                this@AddStoryActivity,
                applicationContext.getString(R.string.empty_image),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.faizurazadri.storyappsubmission1",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path);

            getFile = myFile
            binding.ivItemPhoto.setImageBitmap(result)
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