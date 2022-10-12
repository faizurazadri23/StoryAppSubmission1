package com.faizurazadri.storyappsubmission1.api

import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<CreateAccountResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(@Header("Authorization") token: String): Call<GetStoriesResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : Call<AddNewStoryResponse>
}