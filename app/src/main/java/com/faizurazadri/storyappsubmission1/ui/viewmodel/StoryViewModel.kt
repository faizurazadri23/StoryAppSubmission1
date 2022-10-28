package com.faizurazadri.storyappsubmission1.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faizurazadri.storyappsubmission1.api.ApiConfig
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _error

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse;


    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun createdAccount(
        name: String,
        email: String,
        password: String
    ) {
        _isLoading.value = true

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
        _isLoading.value = true

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

    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {

        return storiesRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun addNewStories(token: String, imageMultipart: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true

        val client =
            ApiConfig.getApiService().addNewStory("Bearer $token", imageMultipart, description)
        client.enqueue(object : Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: Response<AddNewStoryResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _error.value = false
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        _message.value = responseBody.message
                    }

                } else {
                    _error.value = true
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object {
        private const val TAG = "StoryViewModel"

    }
}