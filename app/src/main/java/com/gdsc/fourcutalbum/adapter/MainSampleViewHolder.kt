package com.gdsc.fourcutalbum.adapter

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.gdsc.fourcutalbum.DetailActivity
import com.gdsc.fourcutalbum.R
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.databinding.ListItemMainBinding


class MainSampleViewHolder(private var binding: ListItemMainBinding ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FourCuts) {
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, DetailActivity::class.java)
            intent.putExtra("id", data.id)
            binding.root.context.startActivity(intent)
        }

        data.photo?.let {
            try{
                if(it.equals("")){
                    Glide.with(binding.root.context).load(R.drawable.ic_baseline_broken_image_24)
                        .into(binding.ivItem)
                }else{
                    Glide.with(binding.root.context).load(it)
                        .override(SIZE_ORIGINAL)
                        .apply(RequestOptions().override(600, 600))
                        .into(binding.ivItem)
                }
            }catch(e:Exception){
                Glide.with(binding.root.context).load(R.drawable.ic_baseline_broken_image_24)
                    .into(binding.ivItem)
            }
        }
    }
}