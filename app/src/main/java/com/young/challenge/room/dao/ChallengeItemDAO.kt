package com.young.challenge.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.young.challenge.room.entity.ChallengeItem

@Dao
interface ChallengeItemDAO {
    @Insert
    fun insertChallengeItem(item: ChallengeItem)

    @Query("SELECT * FROM ChallengeItem WHERE challengeName = :name")
    fun getAllItem(name: String): List<ChallengeItem>

    @Query("DELETE FROM ChallengeItem WHERE challengeName = :name")
    fun deleteAllItem(name: String)

    @Query("SELECT COUNT(*) FROM ChallengeItem WHERE challengeName = :name")
    fun countName(name: String): Int

    @Query("UPDATE ChallengeItem SET diary = :diary WHERE imageName = :name")
    fun updateDiary(diary: String, name:String)

    @Query("DELETE FROM ChallengeItem WHERE imageName = :name")
    fun deleteItem(name: String)
}