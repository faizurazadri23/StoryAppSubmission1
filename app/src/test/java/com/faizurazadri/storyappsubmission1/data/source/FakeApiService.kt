package com.faizurazadri.storyappsubmission1.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSource
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSourceTest
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import com.faizurazadri.storyappsubmission1.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FakeApiService : ApiService {

    private val dummyAddNewStory = DataDummy.generateDummyStoryCreateResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyStory = DataDummy.generateDummyStoriesList()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val dummyCreateAccount = DataDummy.generateDummyRegisterResponse()
    private val data = StoriesPagingSourceTest.snapshot(dummyStory)

    override suspend fun createAccount(
        name: String,
        email: String,
        password: String
    ): CreateAccountResponse {
        return dummyCreateAccount
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getAllStories(header: String, page: Int?, size: Int?): GetStoriesResponse {
       return dummyStoryResponse
    }


    override suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): AddNewStoryResponse {
        return dummyAddNewStory
    }

    override suspend fun getStoriesLocation(token: String, id: Int): GetStoriesResponse {
       return dummyStoryResponse
    }
}