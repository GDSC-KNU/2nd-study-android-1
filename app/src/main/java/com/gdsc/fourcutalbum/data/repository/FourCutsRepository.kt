package com.gdsc.fourcutalbum.data.repository

import com.gdsc.fourcutalbum.data.model.FourCuts
import kotlinx.coroutines.flow.Flow

interface FourCutsRepository {

    // Room
    suspend fun insertFourCuts(fourCuts: FourCuts)

    suspend fun deleteFourCuts(fourCuts: FourCuts)

    fun getFourCuts(): Flow<List<FourCuts>>
}