package com.gdsc.fourcutalbum

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.databinding.ActivityDetailBinding
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel

class DetailActivity: AppCompatActivity() {
    lateinit var imageUri: Uri
    lateinit var binding: ActivityDetailBinding
    lateinit var fourCutsViewModel: FourCutsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fourCuts = FourCuts("경주", Uri.EMPTY, listOf("수진", "은경"), "하루필름 황남점", "PROJECT")
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.editIb.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
        binding.titleTv.text = fourCuts.title
        binding.locationTv.text = fourCuts.place
        binding.commentContentTv.text = fourCuts.comment


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