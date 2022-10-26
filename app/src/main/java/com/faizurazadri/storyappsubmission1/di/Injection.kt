package com.faizurazadri.storyappsubmission1.di

import android.content.Context
import com.faizurazadri.storyappsubmission1.api.ApiConfig
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository

object Injection {

    fun provideRepository(context: Context): StoriesRepository {
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(apiService)
    }
}