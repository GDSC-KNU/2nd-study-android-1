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
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EditActivity : AppCompatActivity() {
    lateinit var fourCutsViewModel: FourCutsViewModel
    lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)

        val factory = FourCutsViewModelProviderFactory(fourCutsRepository)
        fourCutsViewModel = ViewModelProvider(this, factory)[FourCutsViewModel::class.java]

        val fin = findViewById<Button>(R.id.editSaveButton)
        val btn = findViewById<ImageView>(R.id.editImageView)
        val plusBtn = findViewById<Button>(R.id.editPlusBtn)
        val group = findViewById<ChipGroup>(R.id.editFriendGroup)
        val loc = findViewById<EditText>(R.id.editLocation)
        val cont = findViewById<EditText>(R.id.editComment)

        btn.setOnClickListener {
            selectGallery()
        }
        plusBtn.setOnClickListener {
            makeDialog(group)
        }
        fin.setOnClickListener {
            val chipList = makeChipList(group)
            val fourCuts =
                FourCuts("경주", imageUri, chipList.toList(), loc.text.toString(), cont.text.toString())
            fourCutsViewModel.saveFourCuts(fourCuts)
            Log.d("database: ", "Insert Data")
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
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                REQ_GALLERY)

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
                    .into(findViewById(R.id.editImageView))
            }
        }
    }
}