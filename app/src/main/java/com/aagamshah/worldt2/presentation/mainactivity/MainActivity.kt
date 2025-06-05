package com.aagamshah.worldt2.presentation.mainactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aagamshah.worldt2.data.remote.RetrofitClient
import com.aagamshah.worldt2.data.repositoryimpl.TeamsRepositoryImpl
import com.aagamshah.worldt2.databinding.ActivityMainBinding
import com.aagamshah.worldt2.presentation.adapter.TeamsAdapter
import com.aagamshah.worldt2.presentation.matchactivity.MatchActivity
import com.aagamshah.worldt2.utils.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(TeamsRepositoryImpl(RetrofitClient.api))
    }
    private lateinit var adapter: TeamsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        initObservers()

    }

    private fun initUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = TeamsAdapter(mutableListOf()) { item, position ->
            viewModel.toggleTeamSelection(item)
        }

        binding.rvTeams.adapter = adapter

        binding.mbStart.setOnClickListener {
            if (viewModel.selectedTeams.size != 2) {
                Toast.makeText(this, "Please select two teams", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MatchActivity::class.java)
            intent.putExtra("TEAM_ONE", viewModel.selectedTeams[0])
            intent.putExtra("TEAM_TWO", viewModel.selectedTeams[1])
            startActivity(intent)
        }

    }

    private fun initObservers() {
        viewModel.teams.observe(this) { result ->
            when (result) {
                is Resource.Error<*> -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Success<*> -> {
                    adapter.addItems(result.data?.toMutableList() ?: mutableListOf())
                }
            }
        }
        viewModel.teams.observe(this) { result ->
            when (result) {
                is Resource.Error<*> -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Success<*> -> {
                    adapter.addItems(result.data?.toMutableList() ?: mutableListOf())

                    binding.mbStart.isEnabled = viewModel.selectedTeams.size == 2
                }
            }
        }

    }

}