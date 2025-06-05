package com.aagamshah.worldt2.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(null)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

}

open class BaseViewHolder(var binding: ViewBinding?) :
    RecyclerView.ViewHolder(binding?.root!!)
