package com.faizurazadri.storyappsubmission1.data.source

import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import com.faizurazadri.storyappsubmission1.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    private val dummyAddNewStory = DataDummy.generateDummyStoryCreateResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val dummyCreateAccount = DataDummy.generateDummyRegisterResponse()

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