package com.example.vvinamp.util

// The server refuses a song that's already in the playlist; its message names it as a duplicate.
// shortcut: matched on the message text because the API has no error code for it.
fun Throwable.isDuplicateTrackError(): Boolean {
    val text = message?.lowercase() ?: return false
    return "duplicate" in text || "already" in text
}
