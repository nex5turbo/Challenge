package com.young.challenge.utils

import android.content.Context
import android.util.DisplayMetrics

object Display {
    var dm: DisplayMetrics? = null

    private var _deviceWidth = -1
    val deviceWidth: Int get() = _deviceWidth

    private var _deviceHeight = -1
    val deviceHeight: Int get() = _deviceHeight

    fun initDM(context: Context) {
        dm = context.resources.displayMetrics
        _deviceWidth = dm!!.widthPixels
        _deviceHeight = dm!!.heightPixels

        dm = null
    }
}