package com.example.smarttvadplayer.domain.model

/**
 * Scrolling direction for scrolling text content.
 */
enum class ScrollDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT
}

/**
 * Scroll speed presets.
 */
enum class ScrollSpeed(val dpPerSecond: Float) {
    SLOW(30f),
    NORMAL(60f),
    FAST(120f)
}
