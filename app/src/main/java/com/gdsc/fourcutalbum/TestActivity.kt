package com.gdsc.fourcutalbum


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TestActivity : AppCompatActivity() {

    lateinit var fourCutsViewModel: FourCutsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)

        val factory = FourCutsViewModelProviderFactory(fourCutsRepository)
        fourCutsViewModel = ViewModelProvider(this,factory)[FourCutsViewModel::class.java]

        val btn = findViewById<Button>(R.id.saveBtn)

        val fourCuts = FourCuts("경주", Uri.EMPTY, listOf("수진", "은경"), "하루필름 황남점", "PROJECT")
        btn.setOnClickListener {
            fourCutsViewModel.saveFourCuts(fourCuts)
            Log.d("database: ", "Insert Data")
        }

        // 쿼리결과 표시
//        lifecycleScope.launch{
//            fourCutsViewModel.getFourCuts.collectLatest { // Flow의 데이터 구독
//                fourCutsAdapter.submitList(it)
//            }
//        }
//        lifecycleScope.launch{
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                fourCutsViewModel.getFourCuts.collectLatest {
//                    Log.d("room db get log", it.toString())
//                }
//            }
//        }


    }
}