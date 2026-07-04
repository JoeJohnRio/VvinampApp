package com.example.data.util

import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDate(): LocalDate {
    if (this.isEmpty()) {
        // In the new core library, it is System instead of Clock.System
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    return LocalDate.parse(this)
}
fun String.dateToYear(): String {
    val dateTime = LocalDateTime.parse(this.replace("Z", ""))
    val year = dateTime.year
    return year.toString()
}