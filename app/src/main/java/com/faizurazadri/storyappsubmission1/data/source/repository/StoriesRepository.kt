package com.faizurazadri.storyappsubmission1.data.source.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSource
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem

class StoriesRepository(private val apiService: ApiService) {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {

        Log.i("DATA TOKEN" , token)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService, token)
            }
        ).liveData
    }
}