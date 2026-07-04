package com.example.vvinamp.util.auth

import java.time.LocalDate
import java.time.Period

fun LocalDate.isAtLeast17YearsOld(): Boolean {
    val today = LocalDate.now()
    val age = Period.between(this, today).years
    return age >= 17
}