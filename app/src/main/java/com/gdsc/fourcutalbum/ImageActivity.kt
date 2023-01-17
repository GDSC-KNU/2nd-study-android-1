package com.gdsc.fourcutalbum

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gdsc.fourcutalbum.adapter.DetailAdapter
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.databinding.ActivityImageBinding
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ImageActivity : AppCompatActivity() {
    lateinit var binding: ActivityImageBinding
    var postId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        binding.imageFull.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

    fun setData(){
        val id = intent.getIntExtra("id",0)
        postId = id
        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)
        val fourCuts = fourCutsRepository.getFourCutsWithId(id).stateIn(lifecycleScope, SharingStarted.WhileSubscribed(5000),
            FourCuts("", Uri.EMPTY, listOf(),"","")
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fourCuts.collectLatest {
                    it.apply {
                        Glide.with(binding.root.context).load(it.photo)
                            .override(Target.SIZE_ORIGINAL)
                            .into(binding.imageFull)
                    }
                }
            }
        }
    }
}