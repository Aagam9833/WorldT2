package com.aagamshah.worldt2.presentation.mainactivity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aagamshah.worldt2.domain.model.TeamsModel
import com.aagamshah.worldt2.domain.repository.TeamsRepository
import com.aagamshah.worldt2.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<TeamsRepository>()
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given api returns failure, then teams LiveData has Failure`() = runTest {

        coEvery { mockRepo.getTeams() } returns Resource.Error(message = "Network failure")
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        advanceUntilIdle()

        assertTrue(mainViewModel.teams.value is Resource.Error)
        assertEquals("Network failure", mainViewModel.teams.value?.message)

    }

    @Test
    fun `selectedTeams contains India`() {

        coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        val result = mainViewModel.selectedTeams[0]
        assertEquals("India", result)

    }

    @Test
    fun `selectedTeams re-toggle removes it`(){

        coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        val result = mainViewModel.selectedTeams
        assertTrue(result.isEmpty())

    }

    @Test
    fun `selecting a team updates the isSelected`() = runTest {

        val fakeTeams = listOf(TeamsModel("India", "IN"))
        coEvery { mockRepo.getTeams() } returns Resource.Success(fakeTeams)

        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        advanceUntilIdle()

        mainViewModel.toggleTeamSelection(TeamsModel("India","IN"))

        val result = mainViewModel.teams.value?.data?.find { it.name == "India" }?.isSelected
        assertEquals(true,result)

    }

    @Test
    fun `selecting a team increases size to 1`(){

        coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        val result = mainViewModel.selectedTeams.size
        assertEquals(1,result)

    }

    @Test
    fun `selecting another team increases size to 2`(){

        coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        mainViewModel.toggleTeamSelection(TeamsModel("Australia","AU",false))
        val result = mainViewModel.selectedTeams.size
        assertEquals(2,result)

    }

    @Test
    fun `selecting third team does not increase size`(){

        coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())
        mainViewModel = MainViewModel(mockRepo, testDispatcher)
        mainViewModel.toggleTeamSelection(TeamsModel("India","IN",false))
        mainViewModel.toggleTeamSelection(TeamsModel("Australia","AUS",false))
        mainViewModel.toggleTeamSelection(TeamsModel("Pakistan","PAK",false))
        val result = mainViewModel.selectedTeams.size
        assertEquals(2,result)

    }

}