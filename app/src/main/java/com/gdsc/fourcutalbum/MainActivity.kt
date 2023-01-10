package com.gdsc.fourcutalbum

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gdsc.fourcutalbum.adapter.MainSampleAdapter
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.databinding.ActivityMainBinding
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory
import com.gdsc.fourcutalbum.viewmodel.MainViewModel
import com.gdsc.fourcutalbum.viewmodel.MainViewModelProviderFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var mainViewModel: MainViewModel
    var dataList : ArrayList<FourCuts> = arrayListOf()
    private var mainAdapter: MainSampleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setInit()
        setDatabase()

        // Room db test
//        var intent : Intent = Intent(MainActivity@this, TestActivity::class.java)
//        startActivity(intent)

    }

    fun setInit(){ binding.btnEdit.setOnClickListener { startActivity(Intent(MainActivity@this, EditActivity::class.java)) } }

    fun setDatabase(){
        val database = FourCutsDatabase.getInstance(this)
        val fourCutsRepository = FourCutsRepositoryImpl(database)

        val factory = MainViewModelProviderFactory(fourCutsRepository)
        mainViewModel = ViewModelProvider(this,factory)[MainViewModel::class.java]

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.getFourCuts.collectLatest {
                    Log.d("room db get log", it.toString())

                }
            }
        }
    }

    fun setRecyclerView(data: List<FourCuts>){
        // init recyclerview
        mainAdapter = MainSampleAdapter()

        mainAdapter?.let {
            it.setListInit(data)
        }

//        binding.rvMain.apply {
//            setHasFixedSize(true)
//            layoutManager = StaggeredGridLayoutManager(applicationContext, )
//            adapter = mainAdapter
//        }
    }
}