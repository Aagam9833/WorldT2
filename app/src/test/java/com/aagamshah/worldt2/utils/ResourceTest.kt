package com.aagamshah.worldt2.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResourceTest {

    @Test
    fun `Resource Success Hello - data equals Hello`(){

        val input = Resource.Success("hello")

        val result = input.data

        assertEquals("hello", result)

    }

    @Test
    fun `Resource Success Hello - message equals null`(){

        val input = Resource.Success("hello")

        val result = input.message

        assertNull(result)

    }

    @Test
    fun `Resource Error oops - message equals oops`(){

        val input = Resource.Error<String>("oops")

        val result = input.message

        assertEquals("oops", result)

    }

    @Test
    fun `Resource Error oops - data equals null`(){

        val input = Resource.Error<String>("oops")

        val result = input.data

        assertNull(result)

    }

}