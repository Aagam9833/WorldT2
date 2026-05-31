package com.aagamshah.worldt2.presentation.matchactivity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aagamshah.worldt2.utils.MatchConstants
import com.aagamshah.worldt2.utils.Status
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var matchViewModel: MatchViewModel

    @Before
    fun setup() {
        matchViewModel = MatchViewModel("Team India", "Team Pakistan")
    }

    @Test
    fun `Initial winner value`() {

        val input = matchViewModel.winner
        val result = input.value
        assertTrue(result.isNullOrEmpty())

    }

    @Test
    fun `Initial outcome value`() {

        val input = matchViewModel.outcome
        val result = input.value
        assertEquals("Match Start", result)

    }

    @Test
    fun `Initial isFirstInnings value`() {

        val input = matchViewModel.isFirstInnings
        val result = input.value
        assertEquals(true, result)

    }

    @Test
    fun `Initial teamOneStats value`() {

        val input = matchViewModel.teamOneStats.value
        val result = input?.status
        assertEquals(Status.BATTING, result)

    }

    @Test
    fun `Initial teamTwoStats value`() {

        val input = matchViewModel.teamTwoStats.value
        val result = input?.status
        assertEquals(Status.BOWLING, result)

    }

    @Test
    fun `Initial logs value`() {

        val input = matchViewModel.logs.value
        val result = input?.size
        assertNotNull(result)
        assertTrue(result != 0)

    }

    @Test
    fun `Adding new log actually adds it`() {

        matchViewModel.addLog("Test event")
        val result = matchViewModel.logs.value?.size
        assertEquals(4, result)

    }

    @Test
    fun `Check last event added`() {

        matchViewModel.addLog("Test event")
        val input = matchViewModel.logs.value
        val result = input?.last()
        assertEquals("Test event", result)

    }

    @Test
    fun `Adding 2 new log actually adds it`() {

        matchViewModel.addLog("Test event1")
        matchViewModel.addLog("Test event2")
        val result = matchViewModel.logs.value?.size
        assertEquals(5, result)

    }

    @Test
    fun `log size after winner`() {

        while (matchViewModel.winner.value.isNullOrBlank()) {
            matchViewModel.playNextBall()
        }

        val totalLogs = matchViewModel.logs.value?.size

        matchViewModel.playNextBall()

        val anotherBall = matchViewModel.logs.value?.size

        assertEquals(totalLogs, anotherBall)

    }

}