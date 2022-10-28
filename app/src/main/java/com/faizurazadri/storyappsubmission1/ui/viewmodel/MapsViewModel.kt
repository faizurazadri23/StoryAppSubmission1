package com.faizurazadri.storyappsubmission1.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.repository.ResultProcess
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository

class MapsViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun getListStories(token: String, id : Int) : LiveData<ResultProcess<List<ListStoryItem>>>{
        return storiesRepository.getStoryLocation(token, id)
    }
}