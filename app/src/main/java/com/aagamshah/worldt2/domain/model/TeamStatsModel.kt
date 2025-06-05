package com.aagamshah.worldt2.domain.model

import com.aagamshah.worldt2.utils.Status

data class TeamStatsModel(
    val status: Status,
    val score: Int,
    val overs: Float,
    val wickets: Int
)
