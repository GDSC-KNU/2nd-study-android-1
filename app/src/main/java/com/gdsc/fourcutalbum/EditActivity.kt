package com.gdsc.fourcutalbum

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.databinding.ActivityEditBinding
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private val binding: ActivityEditBinding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }

    lateinit var fourCutsViewModel: FourCutsViewModel
    private var imageUri: Uri = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)

        val factory = FourCutsViewModelProviderFactory(fourCutsRepository)
        fourCutsViewModel = ViewModelProvider(this, factory)[FourCutsViewModel::class.java]

        val id = intent.getIntExtra("detail_id", 0)
        if (id > 0) setData(id)

        binding.imageIv.setOnClickListener {
            selectGallery()
        }
        binding.editPlusBtn.setOnClickListener {
            makeDialog(binding.editFriendGroup)
        }
        binding.editSaveButton.setOnClickListener {
            val chipList = makeChipList(binding.editFriendGroup)
            if (binding.editTitle.text == null || imageUri == Uri.EMPTY || chipList.size == 0 || binding.editLocation.text == null || binding.editComment.text == null)
                Snackbar.make(it, "입력이 잘못되었습니다.", Snackbar.LENGTH_SHORT).show()
            else {
                val fourCuts =
                    FourCuts(
                        binding.editTitle.text.toString(),
                        imageUri,
                        chipList.toList(),
                        binding.editLocation.text.toString(),
                        binding.editComment.text.toString()
                    )
                if (id > 0) fourCutsViewModel.updateFourCuts(fourCuts.title,
                    fourCuts.photo,
                    fourCuts.friends,
                    fourCuts.place,
                    fourCuts.comment,
                    id)
                else fourCutsViewModel.saveFourCuts(fourCuts)
                finish()
            }
        }

    }

    fun setData(id: Int) {
        //Log.d(":::", "set data")
        val fourCuts = fourCutsViewModel.getFourCutsWithId(id)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                fourCuts.collectLatest {
                    Log.d("TEST", it.toString())
                    it.apply {
                        binding.editTitle.setText(title)
                        binding.editLocation.setText(place)
                        binding.editComment.setText(comment)
                        for (x: String in friends!!) {
                            binding.editFriendGroup.addView(Chip(this@EditActivity).apply {
                                text = x
                                isCloseIconVisible = true
                                setOnCloseIconClickListener {
                                    binding.editFriendGroup.removeView(this)
                                }
                            })
                        }

                        Glide.with(binding.root.context).load(it.photo)
                            .override(Target.SIZE_ORIGINAL)
                            .into(binding.imageIv)

                        imageUri = it.photo
                        //Log.d(":::uri", imageUri.toString())
                    }
                }
            }
        }
    }

    private fun makeChipList(group: ChipGroup): ArrayList<String> {
        val chipList = ArrayList<String>()
        for (i: Int in 1..group.childCount) {
            val chip: Chip = group.getChildAt(i - 1) as Chip
            chipList.add(chip.text.toString())
        }
        return chipList
    }

    private fun makeDialog(group: ChipGroup) {
        val et = EditText(this)
        et.maxLines = 1
        et.inputType = InputType.TYPE_CLASS_TEXT
        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        et.gravity = Gravity.CENTER
        val builder = AlertDialog.Builder(this)
            .setTitle("친구 이름을 적어주세요")
            .setView(et)
            .setPositiveButton("확인") { dialog, which ->
                val string = et.text
                if (string.isNullOrEmpty()) {
                    Toast.makeText(this, "chip 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    group.addView(Chip(this).apply {
                        text = string
                        isCloseIconVisible = true
                        setOnCloseIconClickListener { group.removeView(this) }
                    })
                }
            }
            .setNegativeButton("취소") { dialog, which ->
            }
        builder.show()
    }


    companion object {
        const val REVIEW_MIN_LENGTH = 10

        // 갤러리 권한 요청
        const val REQ_GALLERY = 1

        // API 호출시 Parameter key값
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }

    private fun selectGallery() {
        val writePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        //권한 확인
        if (writePermission == PackageManager.PERMISSION_DENIED ||
            readPermission == PackageManager.PERMISSION_DENIED
        ) {
            // 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQ_GALLERY
            )

        } else {
            // 권한이 있는 경우 갤러리 실행
            val intent = Intent(Intent.ACTION_PICK)
            // uri realpath 권한 이슈 해결
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            // intent의 data와 type을 동시에 설정하는 메서드
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )

            imageResult.launch(intent)
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
                    .into(binding.imageIv)
            }
        }
    }
}