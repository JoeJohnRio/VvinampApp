package com.example.vvinamp.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoverColorTest {

    private fun pixels(vararg groups: Pair<Int, Int>): IntArray =
        groups.flatMap { (argb, count) -> List(count) { argb } }.toIntArray()

    private fun assertClose(expected: Color, actual: Color) {
        val close = abs(expected.red - actual.red) < 0.01f &&
            abs(expected.green - actual.green) < 0.01f &&
            abs(expected.blue - actual.blue) < 0.01f
        assertTrue(close, "expected $expected but was $actual")
    }

    @Test
    fun redCoverGivesDarkMutedRed() {
        val color = pickDarkMutedColor(pixels(0xFFFF0000.toInt() to 100))

        assertClose(Color.hsl(0f, 0.4f, 0.26f), color)
    }

    @Test
    fun mostCommonHueWins() {
        val color = pickDarkMutedColor(
            pixels(0xFF0000FF.toInt() to 70, 0xFFFF0000.toInt() to 30)
        )

        assertClose(Color.hsl(240f, 0.4f, 0.26f), color)
    }

    @Test
    fun blackWhiteAndGreyPixelsAreIgnored() {
        val color = pickDarkMutedColor(
            pixels(
                0xFF000000.toInt() to 500,
                0xFFFFFFFF.toInt() to 500,
                0xFF808080.toInt() to 500,
                0xFF00FF00.toInt() to 10,
            )
        )

        assertClose(Color.hsl(120f, 0.4f, 0.26f), color)
    }

    @Test
    fun coverWithNoUsableColourFallsBackToDarkGrey() {
        val color = pickDarkMutedColor(
            pixels(0xFF000000.toInt() to 50, 0xFFFFFFFF.toInt() to 50, 0xFF808080.toInt() to 50)
        )

        assertEquals(Color.DarkGray, color)
    }

    @Test
    fun transparentPixelsAreIgnored() {
        val color = pickDarkMutedColor(pixels(0x00FF0000 to 100))

        assertEquals(Color.DarkGray, color)
    }
}
