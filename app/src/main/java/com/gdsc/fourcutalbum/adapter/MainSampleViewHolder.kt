package com.gdsc.fourcutalbum.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.fourcutalbum.R
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.databinding.ListItemMainBinding


class MainSampleViewHolder(private var binding: ListItemMainBinding ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FourCuts) {
        data.photo?.let {
            try{
                if(it.equals("")){
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_broken_image_24)
                        .into(binding.ivItem)
                    Log.d("TEST","TEST")
                }else{
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_broken_image_24)
                        .into(binding.ivItem)
                }
            }catch(e:Exception){
                Glide.with(binding.root.context).load(R.drawable.ic_baseline_broken_image_24)
                    .into(binding.ivItem)
            }
        }
    }
}