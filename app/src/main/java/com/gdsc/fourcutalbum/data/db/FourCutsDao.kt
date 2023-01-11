package com.gdsc.fourcutalbum.data.db

import androidx.room.*
import com.gdsc.fourcutalbum.data.model.FourCuts
import kotlinx.coroutines.flow.Flow

@Dao
interface FourCutsDao {

    @Insert
    suspend fun insertFourCuts(fourCuts: FourCuts)

    @Delete
    suspend fun deleteFourCuts(fourCuts: FourCuts)

    @Update
    suspend fun updateFourCuts(fourCuts: FourCuts)

    @Query("SELECT * FROM fourcuts")
    fun getFourCuts() : Flow<List<FourCuts>>

    @Query("SELECT * FROM fourcuts WHERE id LIKE :id")
    fun getFourCutsWithId(id: Int) : Flow<FourCuts>

    @Query("SELECT * FROM fourcuts WHERE friends LIKE :search " + "OR place LIKE :search")
    fun searchFourCuts(search: String): Flow<List<FourCuts>>

}

// @Insert(onConflict = OnConfilictStrategy.REPLACE) PK가 겹치면 대체해줌. 근데 단순 ID라 겹칠 일이 없을 듯.?