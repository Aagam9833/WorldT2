package com.aagamshah.worldt2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aagamshah.worldt2.databinding.CellLogsBinding

class LogsAdapter(val data: MutableList<String>) : BaseAdapter() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val binding = CellLogsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val binding = holder.binding as CellLogsBinding
        binding.tvLog.text = "Log ${position + 1} - ${data[position]}"

    }

    fun addLog(logs: List<String>) {
        data.clear()
        data.addAll(logs)
        notifyDataSetChanged()
    }

}