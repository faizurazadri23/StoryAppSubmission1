package com.faizurazadri.storyappsubmission1.data.source.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.response.GetStoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoriesPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, ListStoryItem>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        val responseData = apiService.getAllStories("Bearer $token", position, params.loadSize)

        return try {
            var datalist = suspendCoroutine<List<ListStoryItem>> { continuation ->
                responseData.enqueue(object : Callback<GetStoriesResponse> {
                    override fun onResponse(
                        call: Call<GetStoriesResponse>,
                        response: Response<GetStoriesResponse>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(
                                (response.body()?.listStory ?: emptyList()) as List<ListStoryItem>
                            )
                        } else {
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }

            LoadResult.Page(
                data = datalist,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (datalist.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}