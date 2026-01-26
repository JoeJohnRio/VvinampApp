package com.example.data.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val datePattern = "yyyy-MM-dd"

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(datePattern)
    if (this.isEmpty()) return LocalDate.now()
    val localDate = LocalDate.parse(this, formatter)
    return localDate
}