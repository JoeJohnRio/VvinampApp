package com.example.vvinamp.ui.main.playback

enum class SleepTimerOption(val label: String, val durationMillis: Long? = null) {
    FIVE_SECONDS("5 seconds", 5000L),
    FIVE_MINUTES("5 minutes", 5 * 60 * 1000L),
    TEN_MINUTES("10 minutes", 10 * 60 * 1000L),
    FIFTEEN_MINUTES("15 minutes", 15 * 60 * 1000L),
    THIRTY_MINUTES("30 minutes", 30 * 60 * 1000L),
    FORTY_FIVE_MINUTES("45 minutes", 45 * 60 * 1000L),
    ONE_HOUR("1 hour", 60 * 60 * 1000L),
    END_OF_TRACK("End of track", null)
}
