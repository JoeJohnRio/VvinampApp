package com.example.data.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun String.toLocalDate(): LocalDate {
    if (this.isEmpty()) {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
    return LocalDate.parse(this)
}

fun String.dateToYear(): String {
    val dateTime = LocalDateTime.parse(this.replace("Z", ""))
    val year = dateTime.year
    return year.toString()
}
