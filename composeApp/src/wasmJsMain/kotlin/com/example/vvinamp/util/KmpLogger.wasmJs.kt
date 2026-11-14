package com.example.vvinamp.util

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

actual object KmpLogger {
    actual fun d(tag: String, message: String) {
        println("$tag: $message")
    }
}

private const val TOAST_VISIBLE_MS = 2500
private const val TOAST_FADE_MS = 300

private var activeToast: HTMLElement? = null

// A plain DOM element sits above the Compose canvas, so the message shows on every screen.
actual fun showToast(message: String) {
    activeToast?.remove()

    val toast = (document.createElement("div") as HTMLElement).apply {
        textContent = message
        // Screen readers announce it without moving focus.
        setAttribute("role", "status")
        setAttribute("aria-live", "polite")
        // Sits above the mini player and the bottom bar; lighter than the sheets and lifted by a shadow so it reads as on top.
        style.cssText = "position:fixed;left:50%;bottom:140px;transform:translateX(-50%);" +
            "max-width:min(90vw,440px);padding:12px 16px;border-radius:8px;" +
            "background:#474747;color:#ffffff;font:14px/1.4 system-ui,sans-serif;" +
            "box-shadow:0 4px 16px rgba(0,0,0,.5);" +
            "z-index:1000;pointer-events:none;opacity:1;transition:opacity ${TOAST_FADE_MS}ms"
    }
    document.body?.appendChild(toast)
    activeToast = toast

    window.setTimeout({
        toast.style.opacity = "0"
        window.setTimeout({
            toast.remove()
            if (activeToast == toast) activeToast = null
            null
        }, TOAST_FADE_MS)
        null
    }, TOAST_VISIBLE_MS)
}
