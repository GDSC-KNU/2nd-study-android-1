package com.gdsc.fourcutalbum

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.gdsc.fourcutalbum.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory


class MainActivity : AppCompatActivity() {

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        with(newConfig) {
            println(orientation)
            println(screenLayout)
        }
    }
    lateinit var binding: ActivityMainBinding



    lateinit var fourCutsViewModel: FourCutsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)

//        val database = FourCutsDatabase.getInstance(this)
//        val fourCutsRepository = FourCutsRepositoryImpl(database)
//
//        val factory = FourCutsViewModelProviderFactory(fourCutsRepository)
//        fourCutsViewModel = ViewModelProvider(this,factory)[FourCutsViewModel::class.java]

        // Room db test
        var intent : Intent = Intent(MainActivity@this, TestActivity::class.java)
        startActivity(intent)

    }
}