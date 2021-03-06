package com.young.challenge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ChallengeItem(
    val challengeName: String,
    val imageName: String,
    val day: Int,
    var diary: String
): Serializable{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
