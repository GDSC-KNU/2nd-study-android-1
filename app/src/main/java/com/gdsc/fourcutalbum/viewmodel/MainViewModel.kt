package com.gdsc.fourcutalbum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdsc.fourcutalbum.data.model.FourCuts
import com.gdsc.fourcutalbum.data.repository.FourCutsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val fourCutsRepository: FourCutsRepository,
) : ViewModel(){

    // Room
    fun saveFourCuts(fourCuts: FourCuts) = viewModelScope.launch(Dispatchers.IO){
        fourCutsRepository.insertFourCuts(fourCuts)
    }

    fun deleteFourCuts(fourCuts: FourCuts) = viewModelScope.launch(Dispatchers.IO){
        fourCutsRepository.deleteFourCuts(fourCuts)
    }

    // TestActivity의 lifecycle과 동기화
    val getFourCuts: StateFlow<List<FourCuts>> = fourCutsRepository.getFourCuts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
}