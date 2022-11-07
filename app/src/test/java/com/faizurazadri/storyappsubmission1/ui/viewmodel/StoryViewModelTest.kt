package com.faizurazadri.storyappsubmission1.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.faizurazadri.storyappsubmission1.adapter.AdapterStory
import com.faizurazadri.storyappsubmission1.data.source.datasource.StoriesPagingSourceTest
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.data.source.repository.ResultProcess
import com.faizurazadri.storyappsubmission1.data.source.repository.StoriesRepository
import com.faizurazadri.storyappsubmission1.data.source.response.AddNewStoryResponse
import com.faizurazadri.storyappsubmission1.data.source.response.CreateAccountResponse
import com.faizurazadri.storyappsubmission1.data.source.response.LoginResponse
import com.faizurazadri.storyappsubmission1.utils.CoroutinesTestRule
import com.faizurazadri.storyappsubmission1.utils.DataDummy
import com.faizurazadri.storyappsubmission1.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoriesRepository
    private lateinit var storyViewModel: StoryViewModel

    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyStory = DataDummy.generateDummyStoriesList()
    private val dummyCreateAccount = DataDummy.generateDummyRegisterResponse()
    private val dummyAddNewStory = DataDummy.generateDummyStoryCreateResponse()

    private val dummyName = "faizura"
    private val dummyEmail = "dicoding@gmail.com"
    private val dummyPassword = "d1coding23"
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUk3cEVNMm92Skk0VW5PUk4iLCJpYXQiOjE2NjcwMTEwMDN9.uUi1X9rudX_Wqj3OZIRCy-S9KtHkWUAH_g-mfZrB3w0"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyMultipartDescription()
    private val data = StoriesPagingSourceTest.snapshot(dummyStory)

    @Before
    fun setUp() {
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `Login successfuly - result success`() {

        val expectedStory = MutableLiveData<ResultProcess<LoginResponse>>()
        expectedStory.value = ResultProcess.Success(dummyLoginResponse)

        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedStory)

        val actualLogin = storyViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is ResultProcess.Success)
        Assert.assertEquals(dummyLoginResponse, (actualLogin as ResultProcess.Success).data)

    }

    @Test
    fun `Register successfuly - result success`() {

        val expectedCreateAccount = MutableLiveData<ResultProcess<CreateAccountResponse>>()
        expectedCreateAccount.value = ResultProcess.Success(dummyCreateAccount)

        `when`(storyRepository.createAccount(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedCreateAccount
        )

        val actualCreateAccount =
            storyViewModel.createdAccount(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(storyRepository).createAccount(dummyName, dummyEmail, dummyPassword)
        Assert.assertNotNull(actualCreateAccount)
        Assert.assertTrue(actualCreateAccount is ResultProcess.Success)
        Assert.assertEquals(dummyCreateAccount, (actualCreateAccount as ResultProcess.Success).data)

    }

    @Test
    fun `Add New Story successfuly - result success`() {

        val expectedAddNewStory = MutableLiveData<ResultProcess<AddNewStoryResponse>>()
        expectedAddNewStory.value = ResultProcess.Success(dummyAddNewStory)

        `when`(
            storyRepository.addNewStory(
                dummyToken,
                dummyMultipart,
                dummyDescription
            )
        ).thenReturn(
            expectedAddNewStory
        )

        val actualAddNewStory =
            storyViewModel.addNewStories(dummyToken, dummyMultipart, dummyDescription)
                .getOrAwaitValue()
        Mockito.verify(storyRepository).addNewStory(dummyToken, dummyMultipart, dummyDescription)
        Assert.assertNotNull(actualAddNewStory)
        Assert.assertTrue(actualAddNewStory is ResultProcess.Success)
        Assert.assertEquals(dummyAddNewStory, (actualAddNewStory as ResultProcess.Success).data)

    }

    @Test
    fun `Get All Story - result success`() = runTest {

        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        `when`(
            storyRepository.getStories(
                dummyToken
            )
        ).thenReturn(expectedStory)

        val actualStory =
            storyViewModel.getAllStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )

        differ.submitData(actualStory)

        Mockito.verify(storyRepository).getStories(
            dummyToken,
        )
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)

    }

    @Test
    fun `Get Story Location - result success`() {

        val expectedStory = MutableLiveData<ResultProcess<List<ListStoryItem>>>()
        expectedStory.value = ResultProcess.Success(dummyStory)

        `when`(
            storyRepository.getStoryLocation(
                dummyToken,
                1
            )
        ).thenReturn(expectedStory)

        val actualStory =
            storyViewModel.getListStoriesLocation(dummyToken, 1).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoryLocation(
            dummyToken,
            1
        )
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is ResultProcess.Success)
        Assert.assertEquals(dummyStory, (actualStory as ResultProcess.Success).data)

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