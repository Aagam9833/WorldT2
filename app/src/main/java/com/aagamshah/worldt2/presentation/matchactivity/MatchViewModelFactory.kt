package com.aagamshah.worldt2.presentation.matchactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MatchViewModelFactory(
    private val teamOneName: String,
    private val teamTwoName: String
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)) {
            return MatchViewModel(teamOneName, teamTwoName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}