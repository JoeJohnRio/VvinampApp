package com.example.vvinamp.util.auth

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun LocalDate.isAtLeast17YearsOld(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val age = this.periodUntil(today).years
    return age >= 17
}
