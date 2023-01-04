package com.gdsc.fourcutalbum.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gdsc.fourcutalbum.data.model.FourCuts
import kotlinx.coroutines.flow.Flow

@Dao
interface FourCutsDao {

    @Insert
    suspend fun insertFourCuts(fourCuts: FourCuts)

    @Delete
    suspend fun deleteFourCuts(fourCuts: FourCuts)

    @Query("SELECT * FROM fourcuts")
    fun getFourCuts() : Flow<List<FourCuts>>

}

// @Insert(onConflict = OnConfilictStrategy.REPLACE) PK가 겹치면 대체해줌. 근데 단순 ID라 겹칠 일이 없을 듯.?