package com.gdsc.fourcutalbum

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gdsc.fourcutalbum.adapter.DetailAdapter
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.databinding.ActivityDetailBinding
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailActivity: AppCompatActivity() {
    lateinit var imageUri: Uri
    lateinit var binding: ActivityDetailBinding
    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: DetailAdapter
    lateinit var viewManager: RecyclerView.LayoutManager
    var postId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setInitialize()
        setData()

    }

    fun setData(){
        val id = intent.getIntExtra("id",0)
        postId = id
        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)
        val fourCuts = fourCutsRepository.getFourCutsWithId(id).stateIn(lifecycleScope, SharingStarted.WhileSubscribed(5000),
            FourCuts("", Uri.EMPTY, listOf(),"",""))
        viewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fourCuts.collectLatest {
                    Log.d("TEST",it.toString())
                    it.apply {
                        binding.titleTv.text = title
                        binding.locationTv.text = place
                        binding.commentContentTv.text = comment // TODO :: friend chip 구현 필요
                        viewAdapter = fourCuts.value.friends?.let { DetailAdapter(it) }!!
                        recyclerView = binding.nameRv.apply {
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                        Glide.with(binding.root.context).load(it.photo)
                            .override(Target.SIZE_ORIGINAL)
                            .apply(RequestOptions().override(600, 600))
                            .into(binding.editImageView)
                    }
                }
            }
        }
    }

    fun setInitialize(){
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.editIb.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            //Log.d("detail id:::", postId.toString())
            intent.putExtra("detail_id", postId)
            startActivity(intent)
        }

    }

    // 이미지를 결과값으로 받는 변수
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // 이미지를 받으면 ImageView에 적용한다
            val temp = result.data?.data
            if (temp != null) {
                imageUri = temp
            }
            imageUri?.let {

                // 이미지를 불러온다
                Glide.with(this)
                    .load(temp)
                    .fitCenter()
                    .apply(RequestOptions().override(500, 500))
                    .into(findViewById(R.id.editImageView))
            }
        }
    }
}