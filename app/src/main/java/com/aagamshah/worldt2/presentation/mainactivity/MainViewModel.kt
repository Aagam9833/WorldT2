package com.aagamshah.worldt2.presentation.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aagamshah.worldt2.domain.model.TeamsModel
import com.aagamshah.worldt2.domain.repository.TeamsRepository
import com.aagamshah.worldt2.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val teamsRepository: TeamsRepository) : ViewModel() {

    private val _teams = MutableLiveData<Resource<List<TeamsModel>>>()
    val teams: LiveData<Resource<List<TeamsModel>>> = _teams

    private val _selectedTeams = mutableListOf<String>()
    val selectedTeams: List<String> get() = _selectedTeams

    init {
        callTeamsApi()
    }

    private fun callTeamsApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = teamsRepository.getTeams()
            when (result) {
                is Resource.Error<*> -> {
                    _teams.postValue(
                        Resource.Error(
                            result.message ?: "Something went wrong"
                        )
                    )
                }

                is Resource.Success<*> -> {
                    _teams.postValue(
                        Resource.Success(
                            result.data ?: emptyList<TeamsModel>()
                        )
                    )
                }
            }
        }
    }

    fun toggleTeamSelection(item: TeamsModel) {
        if (_selectedTeams.contains(item.name)) {
            _selectedTeams.remove(item.name)
        } else {
            if (_selectedTeams.size < 2) {
                _selectedTeams.add(item.name)
            }
        }
        updateTeamsSelection()
    }

    private fun updateTeamsSelection() {
        val currentList = _teams.value?.data ?: emptyList()
        val updatedList = currentList.map {
            it.copy(isSelected = _selectedTeams.contains(it.name))
        }
        _teams.postValue(Resource.Success(updatedList))
    }

    fun clearSelectedTeams() {
        _selectedTeams.clear()
        updateTeamsSelection()
    }

}