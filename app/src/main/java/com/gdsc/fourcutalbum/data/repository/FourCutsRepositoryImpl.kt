package com.gdsc.fourcutalbum.data.repository

import com.gdsc.fourcutalbum.data.db.FourCutsDatabase
import com.gdsc.fourcutalbum.data.model.FourCuts
import kotlinx.coroutines.flow.Flow

class FourCutsRepositoryImpl(
    private val db: FourCutsDatabase
) : FourCutsRepository {
    override suspend fun insertFourCuts(fourCuts: FourCuts) {
        db.fourCutsDao().insertFourCuts(fourCuts)
    }

    override suspend fun deleteFourCuts(fourCuts: FourCuts) {
        db.fourCutsDao().deleteFourCuts(fourCuts)
    }

    override fun getFourCuts(): Flow<List<FourCuts>> {
        return db.fourCutsDao().getFourCuts()
    }


}