package com.young.challenge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ChallengeList(
    @PrimaryKey(autoGenerate = false)
    val challengeName: String,
    val startDate: Long,
    var endDate: Long,
    val kind: Int,
    val isAlarmOn: Boolean,
    val isHideOn: Boolean,
    val alarmAmPm: Boolean,
    val alarmTime: Int,
    val alarmMinute: Int

): Serializable
