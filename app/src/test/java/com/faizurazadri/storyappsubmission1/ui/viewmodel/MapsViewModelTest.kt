package com.faizurazadri.storyappsubmission1.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository
import com.faizurazadri.storyappsubmission1.utils.DataDummy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {


    @Mock
    private lateinit var storiesRepository : StoriesRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStory = DataDummy.generateDummyStoriesEntities()

    @Before
    fun setUp(){
        mapsViewModel = MapsViewModel(storiesRepository);
    }

    @Test
    fun `When Get HeadlineStories Should Not Null and Return success`(){
        val expectedStories = MutableLiveData<Result<List<ListStoryItem>>>()

        expectedStories.value = Result.success(dummyStory)

       // `when`(storiesRepository.getStoryLocation()).thenReturn(expectedStories)
    }
}