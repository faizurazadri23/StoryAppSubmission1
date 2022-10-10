package com.faizurazadri.storyappsubmission1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faizurazadri.storyappsubmission1.api.ApiConfig
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import com.faizurazadri.storyappsubmission1.data.source.response.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse;

    private val _storiesList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storiesList;

    companion object {
        private const val TAG = "StoryViewModel"
    }

    fun createdAccount(
        name: String,
        email: String,
        password: String
    ) {
        _isLoading.value = true;

        val client = ApiConfig.getApiService().createAccount(name, email, password)
        client.enqueue(object : Callback<CreateAccountResponse> {

            override fun onResponse(
                call: Call<CreateAccountResponse>,
                response: Response<CreateAccountResponse>
            ) {
                _isLoading.value = false;

                if (response.isSuccessful) {
                    _error.value = response.body()?.error
                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateAccountResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun login(email: String, password: String) {
        _isLoading.value = true;

        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _error.value = false
                    _loginResponse.value = response.body()
                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getAllStories(token: String) {
        _isLoading.value = true;

        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
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

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}