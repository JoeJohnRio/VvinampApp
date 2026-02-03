package com.example.data.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val datePattern = "yyyy-MM-dd"

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(datePattern)
    if (this.isEmpty()) return LocalDate.now()
    val localDate = LocalDate.parse(this, formatter)
    return localDate
}

fun String.dateToYear(): String {
    val dateTime = LocalDateTime.parse(this.replace("Z", ""))
    val year = dateTime.year
    return year.toString()
}