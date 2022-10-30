package com.faizurazadri.storyappsubmission1.utils

import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.model.LoginResult
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyStoryResponse(): GetStoriesResponse {
        val error= false
        val message = "Stories fetched successfully"
        val listStoryItem = generateDummyStoriesList()

        return GetStoriesResponse(listStoryItem, error, message)
    }

    fun generateDummyStoryCreateResponse(): AddNewStoryResponse {
        val error = false
        val message = "Stories fetched successfully"

        return AddNewStoryResponse(error, message)
    }

    fun generateDummyStoriesList(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()

        for (i in 0..10) {
            val stories = ListStoryItem(
                "story-D2VecBT0ZLNsGeBN",
                "tess",
                "Testing data",
                -6.342342342342342,
                106.87030642297793,
                "https://story-api.dicoding.dev/images/stories/photos-1667052100665_TbntNCXd.jpg",
                "2022-10-29T14:01:40.668Z"
            )

            storyList.add(stories)
        }

        return storyList
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-MmXOTz_I-k7_ORnA",
            name = "faizura zadri",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUk3cEVNMm92Skk0VW5PUk4iLCJpYXQiOjE2NjcwMTEwMDN9.uUi1X9rudX_Wqj3OZIRCy-S9KtHkWUAH_g-mfZrB3w0"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): CreateAccountResponse {

        return CreateAccountResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "image"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyMultipartDescription(): RequestBody {
        val dummyText = "description"
        return dummyText.toRequestBody()
    }
}