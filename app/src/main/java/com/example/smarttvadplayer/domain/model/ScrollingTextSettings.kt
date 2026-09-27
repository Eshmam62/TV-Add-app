package com.example.smarttvadplayer.domain.model

/**
 * Scrolling text display settings for a zone.
 */
data class ScrollingTextSettings(
    val text: String = "",
    val textSize: Float = 32f,
    val textColor: String = "#FFFFFF",
    val backgroundColor: String = "#00000000",
    val scrollDirection: ScrollDirection = ScrollDirection.RIGHT_TO_LEFT,
    val scrollSpeed: ScrollSpeed = ScrollSpeed.NORMAL,
    val durationMs: Long = 0
)
