package com.faizurazadri.storyappsubmission1.data.source.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse

class QuotePagingSource(private  val apiService: ApiService) :
    PagingSource<Int, GetStoriesResponse>() {
    override fun getRefreshKey(state: PagingState<Int, GetStoriesResponse>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetStoriesResponse> {
        TODO("Not yet implemented")
    }
}