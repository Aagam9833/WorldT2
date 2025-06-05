package com.aagamshah.worldt2.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.aagamshah.worldt2.R
import com.aagamshah.worldt2.databinding.CellTeamBinding
import com.aagamshah.worldt2.domain.model.TeamsModel
import com.bumptech.glide.Glide

class TeamsAdapter(
    val data: MutableList<TeamsModel>,
    val listener: (TeamsModel, Int) -> Unit
) : BaseAdapter() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val binding = CellTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        val binding = holder.binding as CellTeamBinding
        val item = data[position]

        Glide.with(holder.itemView.context).load(item.flag).circleCrop().into(binding.ivFlag)
        binding.tvCountry.text = item.name

        val color = if (item.isSelected) R.color.gray else R.color.white
        binding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, color))

        binding.root.setOnClickListener {
            listener(item, position)
        }

        if (position == data.size - 1) {
            binding.divider.visibility = View.GONE
        }

    }

    fun addItems(items: MutableList<TeamsModel>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

}