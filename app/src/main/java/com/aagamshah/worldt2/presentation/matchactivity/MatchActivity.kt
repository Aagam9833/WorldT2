package com.aagamshah.worldt2.presentation.matchactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.aagamshah.worldt2.R
import com.aagamshah.worldt2.databinding.ActivityMatchBinding
import com.aagamshah.worldt2.utils.Status

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private lateinit var viewModel: MatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        initUI()
        initObservers()

    }

    private fun init() {
        val teamOne = intent.getStringExtra("TEAM_ONE") ?: ""
        val teamTwo = intent.getStringExtra("TEAM_TWO") ?: ""
        viewModel = ViewModelProvider(
            this,
            MatchViewModelFactory(teamOne, teamTwo)
        )[MatchViewModel::class.java]
    }

    private fun initObservers() {

        viewModel.teamOneStats.observe(this) { stats ->
            if (stats.status == Status.BATTING) {
                binding.tvTeamOneStatus.text = getString(R.string.batting)
            } else {
                binding.tvTeamOneStatus.text = getString(R.string.bowling)
            }
            val scoreText = "Score: ${stats.score}/${stats.wickets}"
            binding.tvTeamOneScore.text = scoreText
            val oversText = "Overs: ${stats.overs}"
            binding.tvTeamOneOvers.text = oversText
        }

        viewModel.teamTwoStats.observe(this) { stats ->

            if (stats.status == Status.BATTING) {
                binding.tvTeamTwoStatus.text = getString(R.string.batting)
            } else {
                binding.tvTeamTwoStatus.text = getString(R.string.bowling)
            }

            if (viewModel.isFirstInnings.value == true) {
                val scoreText = "Yet To Bat"
                binding.tvTeamTwoScore.text = scoreText
                val oversText = "Yet To Bat"
                binding.tvTeamTwoOvers.text = oversText
            } else {
                val scoreText = "Score: ${stats.score}/${stats.wickets}"
                binding.tvTeamTwoScore.text = scoreText
                val oversText = "Overs: ${stats.overs}"
                binding.tvTeamTwoOvers.text = oversText
            }
        }

        viewModel.outcome.observe(this) { outcome ->
            binding.tvOutcome.text = outcome
        }

        viewModel.winner.observe(this) { winner ->
            binding.tvOutcome.text = winner
            if (winner.isNotBlank()) {
                binding.mbPlay.text = getString(R.string.match_over)
            }
        }

    }

    private fun initUI() {
        binding = ActivityMatchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvBack.setOnClickListener {
            finish()
        }

        binding.tvTeamOne.text = viewModel.teamOneName
        binding.tvTeamTwo.text = viewModel.teamTwoName

        binding.mbPlay.setOnClickListener {
            val winner = viewModel.winner.value
            if (!winner.isNullOrBlank()) {
                finish()
            }

            viewModel.playNextBall()
        }

    }
}