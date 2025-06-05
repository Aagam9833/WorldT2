package com.aagamshah.worldt2.data.repositoryimpl

import com.aagamshah.worldt2.data.remote.ApiService
import com.aagamshah.worldt2.domain.model.TeamsModel
import com.aagamshah.worldt2.domain.model.toDomainModel
import com.aagamshah.worldt2.domain.repository.TeamsRepository
import com.aagamshah.worldt2.utils.Resource

class TeamsRepositoryImpl(private val apiService: ApiService) : TeamsRepository {
    override suspend fun getTeams(): Resource<List<TeamsModel>> {
        return try {
            val response = apiService.getTeams()

            if (response.isSuccessful) {
                Resource.Success(response.body()?.toDomainModel() ?: emptyList())
            } else {
                Resource.Error(response.errorBody().toString())
            }

        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Something went wrong")
        }
    }

}