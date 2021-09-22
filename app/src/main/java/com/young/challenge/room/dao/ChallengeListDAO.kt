package com.young.challenge.room.dao

import androidx.room.*
import com.young.challenge.room.entity.ChallengeList

@Dao
interface ChallengeListDAO {
    @Insert
    fun insertChallengeList(item: ChallengeList)

    @Update
    fun updateChallengeList(item: ChallengeList)

    @Query("SELECT * FROM ChallengeList")
    fun getAllList(): List<ChallengeList>

    @Query("SELECT COUNT(*) FROM ChallengeList WHERE challengeName = :name")
    fun countName(name: String): Int
}