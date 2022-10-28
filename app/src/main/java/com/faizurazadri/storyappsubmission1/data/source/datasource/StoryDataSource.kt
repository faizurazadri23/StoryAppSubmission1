package com.faizurazadri.storyappsubmission1.data.source.datasource

import com.faizurazadri.storyappsubmission1.api.ApiService

class StoryDataSource(private val apiService: ApiService, private val token: String) {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    fun getAllStoryLocation(id: Int) {

    }
}