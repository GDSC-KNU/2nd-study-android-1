package com.gdsc.fourcutalbum

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.repository.FourCutsRepositoryImpl
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModel
import com.gdsc.fourcutalbum.viewmodel.FourCutsViewModelProviderFactory


class MainActivity : AppCompatActivity() {

    lateinit var fourCutsViewModel: FourCutsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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