package com.faizurazadri.storyappsubmission1.data.source.response

import android.os.Parcelable
import com.faizurazadri.storyappsubmission1.data.source.model.LoginResult
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable
