package com.example.vvinamp.util

import kotlin.test.Test
import kotlin.test.assertEquals

class DurationLabelTest {

    @Test
    fun underAMinuteShowsSeconds() {
        assertEquals("45 sec", 45.toDurationLabel())
    }

    @Test
    fun underAnHourShowsMinutesAndSeconds() {
        // 616 seconds was shown as "616min" before.
        assertEquals("10 min 16 sec", 616.toDurationLabel())
    }

    @Test
    fun anHourOrMoreShowsHoursAndMinutes() {
        assertEquals("1 hr 5 min", 3_923.toDurationLabel())
    }

    @Test
    fun emptyOrNegativeShowsZero() {
        assertEquals("0 sec", 0.toDurationLabel())
        assertEquals("0 sec", (-5).toDurationLabel())
    }
}
