package com.aagamshah.worldt2.domain.model

import com.aagamshah.worldt2.data.model.TeamsModel as DataTeamsModel
import com.aagamshah.worldt2.domain.model.TeamsModel as DomainTeamsModel
import org.junit.Assert.assertEquals
import org.junit.Test

class TeamsModelMappingTest {

    @Test
    fun `Empty List Input - Result is Empty`() {
        val input = emptyList<DataTeamsModel>()
        val result = input.toDomainModel()
        assertEquals(0, result.size)
    }

    @Test
    fun `Single item - name and flag are preserved`() {
        val input = listOf(DataTeamsModel("India", "Indian Flag"))
        val result = input.toDomainModel()[0]
        assertEquals(DomainTeamsModel("India", "Indian Flag", false), result)
    }

    @Test
    fun `Multiple items - result size matches input size`(){
        val input = listOf(DataTeamsModel("India","Indian Flag"), DataTeamsModel("Australia","Australian Flag"), DataTeamsModel("England","England Flag"))
        val result = input.toDomainModel()
        assertEquals(3, result.size)
    }

    @Test
    fun `Item passed in with any isSelected value - isSelected is always false in the result`() {
        val input = listOf(DataTeamsModel("India", "Indian Flag"))
        val result = input.toDomainModel()[0].isSelected
        assertEquals(false, result)
    }

}