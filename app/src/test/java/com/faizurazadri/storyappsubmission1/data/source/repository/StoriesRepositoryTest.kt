package com.faizurazadri.storyappsubmission1.data.source.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.faizurazadri.storyappsubmission1.adapter.AdapterStory
import com.faizurazadri.storyappsubmission1.api.ApiService
import com.faizurazadri.storyappsubmission1.data.source.FakeApiService
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSourceTest
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.ui.viewmodel.StoryViewModel
import com.faizurazadri.storyappsubmission1.utils.CoroutinesTestRule
import com.faizurazadri.storyappsubmission1.utils.DataDummy
import com.faizurazadri.storyappsubmission1.utils.getOrAwaitValue
import com.faizurazadri.storyappsubmission1.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()


    @Mock
    private lateinit var apiService: ApiService

    private lateinit var storiesRepository: StoriesRepository

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyStory = DataDummy.generateDummyStoriesList()
    private val dummyCreateAccount = DataDummy.generateDummyRegisterResponse()
    private val dummyAddNewStory = DataDummy.generateDummyStoryCreateResponse()

    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()

    private val dummyName = "faizura"
    private val dummyEmail = "dicoding@gmail.com"
    private val dummyPassword = "d1coding23"
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUk3cEVNMm92Skk0VW5PUk4iLCJpYXQiOjE2NjcwMTEwMDN9.uUi1X9rudX_Wqj3OZIRCy-S9KtHkWUAH_g-mfZrB3w0"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyMultipartDescription()
    private val data = StoriesPagingSourceTest.snapshot(dummyStory)
    private lateinit var storyViewModel: StoryViewModel


    @Before
    fun setup() {
        apiService = FakeApiService()
        storiesRepository = StoriesRepository(apiService)
        storyViewModel = StoryViewModel(storiesRepository)
    }


    @Test
    fun `User Login successfully`(): Unit = runTest {

        val expectedLogin = dummyLoginResponse


        val actualLogin = storiesRepository.login(dummyEmail, dummyPassword)

        actualLogin.observeForTesting {
            Assert.assertNotNull(actualLogin)
            Assert.assertEquals(
                expectedLogin.loginResult,
                (actualLogin.value as ResultProcess.Success).data.loginResult
            )
        }

    }

    @Test
    fun `Register successfuly - result success`(): Unit = runTest {

        val expectedCreateAccount = dummyCreateAccount

        val actualCreateAccount =
            storiesRepository.createAccount(dummyName, dummyEmail, dummyPassword)

        actualCreateAccount.observeForTesting {
            Assert.assertNotNull(actualCreateAccount)
            Assert.assertEquals(
                expectedCreateAccount,
                (actualCreateAccount.value as ResultProcess.Success).data
            )
        }
    }

    @Test
    fun `Add New Story successfuly - result success`(): Unit = runTest {

        val expectedAddNewStory = dummyAddNewStory

        val actualAddNewStory =
            storiesRepository.addNewStory(dummyToken, dummyMultipart, dummyDescription)

        actualAddNewStory.observeForTesting {
            Assert.assertNotNull(actualAddNewStory)
            Assert.assertEquals(
                expectedAddNewStory,
                (actualAddNewStory.value as ResultProcess.Success).data
            )
        }

    }

    @Test
    fun `Get Story Location - result success`(): Unit = runTest {

        val expectedStoryLocation = dummyStoryResponse

        val actualStoryLocation =
            storiesRepository.getStoryLocation(dummyToken, 1)

        actualStoryLocation.observeForTesting {
            Assert.assertNotNull(actualStoryLocation)
            Assert.assertEquals(
                expectedStoryLocation.listStory.size,
                (actualStoryLocation.value as ResultProcess.Success).data.size
            )
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
        }

        override fun onRemoved(position: Int, count: Int) {
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {

        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
        }
    }
}