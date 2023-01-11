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

    override suspend fun updateFourCuts(fourCuts: FourCuts) {
        db.fourCutsDao().updateFourCuts(fourCuts)
    }

    override fun getFourCuts(): Flow<List<FourCuts>> {
        return db.fourCutsDao().getFourCuts()
    }

    override fun getFourCutsWithId(id: Int): Flow<FourCuts> {
        return db.fourCutsDao().getFourCutsWithId(id)
    }

    override fun searchFourCuts(search: String): Flow<List<FourCuts>> {
        return db.fourCutsDao().searchFourCuts(search)
    }


}