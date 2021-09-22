package com.young.challenge.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.young.challenge.room.dao.ChallengeItemDAO
import com.young.challenge.room.dao.ChallengeListDAO
import com.young.challenge.room.entity.ChallengeItem
import com.young.challenge.room.entity.ChallengeList

@Database(version = 1, entities = [ChallengeItem::class, ChallengeList::class])
abstract class MyDatabase: RoomDatabase() {
    abstract fun challengeItemDao(): ChallengeItemDAO
    abstract fun challengeListDao(): ChallengeListDAO

    companion object {
        private var instance: MyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabase? {
            if (instance == null) {
                synchronized(MyDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "challenge-camera.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}