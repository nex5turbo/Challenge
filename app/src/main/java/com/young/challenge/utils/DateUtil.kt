package com.young.challenge.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun dateDifference(startDate: Long, endDate: Long, kind: Int): Int{ // kind 0 -> count up, opp -> count down
        var realEndDate = endDate
        var realStartDate = startDate
        if (kind == 0) {
            realEndDate = System.currentTimeMillis()
        }
        else {
            realStartDate = System.currentTimeMillis()
        }
        val sf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val mEndDate =  sf.format(realEndDate)
        val mStartDate = sf.format(realStartDate)

        val difference = sf.parse(mEndDate).time - sf.parse(mStartDate).time
        val seconds = difference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days.toInt()
    }
}