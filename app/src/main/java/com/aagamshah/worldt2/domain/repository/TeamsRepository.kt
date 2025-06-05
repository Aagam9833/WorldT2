package com.aagamshah.worldt2.domain.repository

import com.aagamshah.worldt2.domain.model.TeamsModel
import com.aagamshah.worldt2.utils.Resource

interface TeamsRepository {

    suspend fun getTeams(): Resource<List<TeamsModel>>

}