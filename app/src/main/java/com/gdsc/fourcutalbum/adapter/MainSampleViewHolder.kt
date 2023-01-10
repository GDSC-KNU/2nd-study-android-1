package com.gdsc.fourcutalbum.adapter

import androidx.recyclerview.widget.RecyclerView
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.databinding.ListItemMainBinding


class MainSampleViewHolder(private var binding: ListItemMainBinding ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FourCuts) {
        data.photo?.let {
            binding.ivItem.setImageURI(it)
        }
    }
}