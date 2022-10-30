package com.faizurazadri.storyappsubmission1.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.repository.ResultProcess
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun createdAccount(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultProcess<CreateAccountResponse>> {
        return storiesRepository.createAccount(name, email, password);
    }

    fun login(email: String, password: String): LiveData<ResultProcess<LoginResponse>> {

        return storiesRepository.login(email, password);
    }

    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {

        return storiesRepository.getStories(token).cachedIn(viewModelScope)
    }

    fun addNewStories(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): LiveData<ResultProcess<AddNewStoryResponse>> {

        return storiesRepository.addNewStory(token, imageMultipart, description)
    }

    fun getListStoriesLocation(token: String, id : Int) : LiveData<ResultProcess<List<ListStoryItem>>>{
        return storiesRepository.getStoryLocation(token, id)
    }
}