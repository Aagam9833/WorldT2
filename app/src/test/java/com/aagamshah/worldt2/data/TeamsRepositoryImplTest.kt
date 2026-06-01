package com.aagamshah.worldt2.data

import com.aagamshah.worldt2.data.model.TeamsModel
import com.aagamshah.worldt2.data.remote.ApiService
import com.aagamshah.worldt2.data.repositoryimpl.TeamsRepositoryImpl
import com.aagamshah.worldt2.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TeamsRepositoryImplTest {

    private val mockApiService = mockk<ApiService>()
    private lateinit var teamsRepositoryImpl: TeamsRepositoryImpl

    @Before
    fun setUp() {
        teamsRepositoryImpl = TeamsRepositoryImpl(mockApiService)
    }

    @Test
    fun `given api returns 200 with body, when getTeams called, then returns Resource Success`() =
        runTest {
            val fakeBody = listOf(TeamsModel(name = "India", flag = "🇮🇳"))
            coEvery { mockApiService.getTeams() } returns Response.success(fakeBody)

            val result = teamsRepositoryImpl.getTeams()

            assertTrue(result is Resource.Success)
            assertEquals(1, result.data?.size)
            assertEquals("India", result.data?.get(0)?.name)
            assertEquals(false, result.data?.get(0)?.isSelected)
        }

    @Test
    fun `given api returns non 200 response, then returns Resource Failure`() = runTest {

        coEvery { mockApiService.getTeams() } returns Response.error(
            500,
            ResponseBody.create(null, "Server Error")
        )

        val result = teamsRepositoryImpl.getTeams()

        assertTrue(result is Resource.Error)
        assertTrue(!result.message.isNullOrBlank())

    }

    @Test
    fun `given api throws No connection exception, then returns Resource Failure with No connection`() =
        runTest {

            coEvery { mockApiService.getTeams() } throws IOException("No connection")

            val result = teamsRepositoryImpl.getTeams()

            assertTrue(result is Resource.Error)
            assertEquals("No connection", result.message)

        }

    @Test
    fun `given api throws any exception, then returns Resource Failure with Something went wrong`() =
        runTest {

            coEvery { mockApiService.getTeams() } throws Exception()

            val result = teamsRepositoryImpl.getTeams()

            assertTrue(result is Resource.Error)
            assertEquals("Something went wrong", result.message)

        }

}