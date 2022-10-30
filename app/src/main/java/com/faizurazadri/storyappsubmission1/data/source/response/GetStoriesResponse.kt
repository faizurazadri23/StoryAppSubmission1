package com.faizurazadri.storyappsubmission1.data.source.response

import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.google.gson.annotations.SerializedName

data class GetStoriesResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
