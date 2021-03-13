package com.mayandro.common.dataandtime

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatUnixTimeLong(uiPattern: String): String {
    val targetFormat = SimpleDateFormat(uiPattern, Locale.ENGLISH)
    val date = Date(this*1000)
    return targetFormat.format(date)
}
