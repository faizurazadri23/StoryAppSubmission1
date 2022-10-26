package com.faizurazadri.storyappsubmission1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faizurazadri.storyappsubmission1.api.ApiConfig
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesLocation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error

    private val _storiesList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storiesList


    fun getStoriesLocation(token: String, id: Int) {
        _isLoading.value = true;

        val client = ApiConfig.getApiService().getStoriesLocation("Bearer $token", id)
        client.enqueue(object : Callback<GetStoriesLocation> {
            override fun onResponse(
                call: Call<GetStoriesLocation>,
                response: Response<GetStoriesLocation>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _error.value = false
                    _storiesList.value = response.body()?.listStory as List<ListStoryItem>?
                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetStoriesLocation>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MapsViewModel"

    }
}