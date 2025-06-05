package com.aagamshah.worldt2.presentation.matchactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aagamshah.worldt2.domain.model.TeamStatsModel
import com.aagamshah.worldt2.utils.MatchConstants
import com.aagamshah.worldt2.utils.Outcome
import com.aagamshah.worldt2.utils.Status

class MatchViewModel(
    val teamOneName: String,
    val teamTwoName: String
) : ViewModel() {

    private var currentBall = 0
    private var currentScore = 0
    private var currentWickets = 0
    private var targetScore = 0
    private var isFreeHit = false

    private val _winner = MutableLiveData<String>()
    val winner: LiveData<String> = _winner

    private val _teamOneStats = MutableLiveData<TeamStatsModel>()
    val teamOneStats: LiveData<TeamStatsModel> = _teamOneStats

    private val _teamTwoStats = MutableLiveData<TeamStatsModel>()
    val teamTwoStats: LiveData<TeamStatsModel> = _teamTwoStats

    private val _isFirstInnings = MutableLiveData<Boolean>()
    val isFirstInnings: LiveData<Boolean> = _isFirstInnings

    private val _outcome = MutableLiveData<String>()
    val outcome: LiveData<String> = _outcome

    private val _logs = MutableLiveData<List<String>>()
    val logs: LiveData<List<String>> = _logs

    init {
        _logs.value = emptyList<String>()
        _winner.value = ""
        _outcome.value = "Match Start"
        _teamOneStats.value = TeamStatsModel(Status.BATTING, 0, 0.0f, 0)
        _teamTwoStats.value = TeamStatsModel(Status.BOWLING, 0, 0.0f, 0)
        _isFirstInnings.value = true
        addLog("$teamOneName Vs $teamTwoName")
        addLog("Match Start")
        addLog("$teamOneName Batting")
    }

    fun addLog(event: String) {
        val updatedLogs = _logs.value.orEmpty().toMutableList().apply { add(event) }
        _logs.value = updatedLogs
    }

    fun playNextBall() {
        if (_winner.value?.isNotBlank() == true) return

        val outcome = simulateBall()
        updateScore(outcome)

        val overs = calculateOvers(currentBall)
        _outcome.value = outcome.displayText

        addLog("Ball: $currentBall, Score: $currentScore, Wickets: $currentWickets, Outcome: ${outcome.displayText}")

        if (_isFirstInnings.value == true) {
            _teamOneStats.value = _teamOneStats.value?.copy(
                score = currentScore,
                overs = overs,
                wickets = currentWickets
            )

            if (isInningsOver()) {
                targetScore = currentScore + 1
                resetAndSwitch()
            }

        } else {
            _teamTwoStats.value = _teamTwoStats.value?.copy(
                score = currentScore,
                overs = overs,
                wickets = currentWickets
            )

            if (currentScore >= targetScore) {
                _winner.value = "$teamTwoName Wins"
                addLog("$teamTwoName Wins")
            } else if (isInningsOver()) {
                if (currentScore == targetScore - 1) {
                    _winner.value = "Match Drawn"
                    addLog("$teamTwoName Wins")
                } else {
                    _winner.value = "$teamOneName Wins"
                    addLog("$teamTwoName Wins")
                }
            }
        }

    }

    private fun updateScore(outcome: Outcome) {
        when (outcome) {
            Outcome.ZERO -> currentScore += 0
            Outcome.ONE -> currentScore += 1
            Outcome.TWO -> currentScore += 2
            Outcome.THREE -> currentScore += 3
            Outcome.FOUR -> currentScore += 4
            Outcome.SIX -> currentScore += 6
            Outcome.OUT -> {
                if (isFreeHit) {
                    isFreeHit = false
                } else {
                    currentWickets++
                }
            }

            Outcome.WIDE -> {
                currentScore += 1
                currentBall--
            }

            Outcome.NO_BALL -> {
                isFreeHit = true
                currentScore += 1
                currentBall--
            }
        }
        currentBall++
    }

    private fun calculateOvers(ballCount: Int): Float {
        return (ballCount / MatchConstants.BALLS_PER_OVER).toFloat() +
                (ballCount % MatchConstants.BALLS_PER_OVER) / 10f
    }

    private fun isInningsOver(): Boolean {
        return currentBall >= MatchConstants.TOTAL_BALLS || currentWickets >= MatchConstants.MAX_WICKETS
    }

    private fun resetAndSwitch() {
        _isFirstInnings.value = false
        currentBall = 0
        currentScore = 0
        currentWickets = 0
        _teamOneStats.value = _teamOneStats.value?.copy(
            status = Status.BOWLING,
        )
        _teamTwoStats.value = _teamTwoStats.value?.copy(
            status = Status.BATTING,
        )
        addLog("$teamTwoName Batting")
    }

    private fun simulateBall(): Outcome {
        val list = mutableListOf<Outcome>()

        for (outcome in Outcome.entries) {
            repeat(outcome.probability) {
                list.add(outcome)
            }
        }

        return list.random()
    }

}