package com.aagamshah.worldt2.data.remote

import com.aagamshah.worldt2.data.model.TeamsModel
import com.aagamshah.worldt2.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.TEAMS)
    suspend fun getTeams(): Response<List<TeamsModel>>

}