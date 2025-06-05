package com.aagamshah.worldt2.domain.model

import com.aagamshah.worldt2.data.model.TeamsModel

data class TeamsModel(
    val name: String,
    val flag: String,
    var isSelected: Boolean = false
)


fun List<TeamsModel>.toDomainModel(): List<com.aagamshah.worldt2.domain.model.TeamsModel> {
    return this.map {
        TeamsModel(
            name = it.name,
            flag = it.flag,
            isSelected = false
        )
    }
}