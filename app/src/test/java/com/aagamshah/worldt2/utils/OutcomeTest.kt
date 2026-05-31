package com.aagamshah.worldt2.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OutcomeTest {

    @Test
    fun `No outcome has a blank displayText - All entries have non-empty display text`() {

        val input = Outcome.entries

        val result = input.map { it.displayText }

        result.forEach {
            assertTrue(it.isNotEmpty())
        }

    }

    @Test
    fun `No outcome has a zero or negative probability - All entries have probability greater than 0`() {

        val input = Outcome.entries

        val result = input.map { it.probability }

        result.forEach {
            assertTrue(it > 0)
        }

    }

    @Test
    fun `Total probability across all entries equals 21 - Sum of all probability values`(){

        val input = Outcome.entries

        val result = input.sumOf { it.probability }

        assertEquals(21, result)

    }

}