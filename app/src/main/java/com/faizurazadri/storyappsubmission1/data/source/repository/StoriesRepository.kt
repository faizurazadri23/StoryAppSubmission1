package com.faizurazadri.storyappsubmission1.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSource
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoriesRepository(private val apiService: ApiService) {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {

        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService, token)
            }
        ).liveData
    }


    fun login(email: String, password: String): LiveData<ResultProcess<LoginResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiService.login(email, password)

            emit(ResultProcess.Success(response))

        } catch (e: Exception) {
            Log.e("story view", "onFailure: " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun createAccount(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultProcess<CreateAccountResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiService.createAccount(name, email, password)

            emit(ResultProcess.Success(response))

        } catch (e: Exception) {
            Log.e("story view", "onFailure: " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }


    fun addNewStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ResultProcess<AddNewStoryResponse>> = liveData {
        emit(ResultProcess.Loading)

        try {
            val response = apiService.addNewStory("Bearer $token", imageMultipart, description)

            emit(ResultProcess.Success(response))

        } catch (e: Exception) {
            Log.e("story view", "onFailure: " + e.message.toString())
            emit(ResultProcess.Error(e.message.toString()))
        }
    }

    fun getStoryLocation(token: String, id: Int): LiveData<ResultProcess<List<ListStoryItem>>> =
        liveData {


            emit(ResultProcess.Loading)

            try {
                val response = apiService.getStoriesLocation("Bearer $token", id)

                val storyItem = response.listStory
                val storyItemList = storyItem.map { storyItems ->
                    ListStoryItem(
                        storyItems?.id,
                        storyItems?.name,
                        storyItems?.description,
                        storyItems?.lat,
                        storyItems?.lon,
                        storyItems?.photoUrl,
                        storyItems?.createdAt
                    )
                }
                emit(ResultProcess.Success(storyItemList))

            } catch (e: Exception) {

                emit(ResultProcess.Error(e.message.toString()))
            }
        }
}

