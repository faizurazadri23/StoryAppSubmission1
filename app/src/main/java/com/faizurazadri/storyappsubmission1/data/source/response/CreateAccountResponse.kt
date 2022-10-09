package com.faizurazadri.storyappsubmission1.data.source.response

import com.google.gson.annotations.SerializedName

data class CreateAccountResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null

)
