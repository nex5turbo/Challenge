package com.young.challenge.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ChallengeList(
    @PrimaryKey(autoGenerate = false)
    var challengeName: String,
    var startDate: Long,
    var endDate: Long,
    var kind: Int,
    var isAlarmOn: Boolean,
    var isHideOn: Boolean,
    var alarmAmPm: Boolean,
    var alarmTime: Int,
    var alarmMinute: Int

): Serializable
