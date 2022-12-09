package com.parade621.community_app.utils

import java.text.SimpleDateFormat
import java.util.*

class Time {

    companion object{
        fun getTime():String{
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.mm.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

            return dateFormat
        }
    }
}