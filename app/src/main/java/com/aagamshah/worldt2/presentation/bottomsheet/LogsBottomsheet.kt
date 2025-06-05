package com.aagamshah.worldt2.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aagamshah.worldt2.databinding.BsLogsBinding
import com.aagamshah.worldt2.presentation.adapter.LogsAdapter
import com.aagamshah.worldt2.presentation.matchactivity.MatchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogsBottomsheet() :
    BottomSheetDialogFragment() {

    private val viewModel: MatchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BsLogsBinding.inflate(inflater, container, false)

        val adapter = LogsAdapter(mutableListOf())
        binding.rvLogs.adapter = adapter

        viewModel.logs.observe(viewLifecycleOwner) { log ->
            adapter.addLog(log)
        }

        return binding.root
    }


}