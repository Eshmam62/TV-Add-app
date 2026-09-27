package com.example.smarttvadplayer.domain.model

/**
 * Represents the selected layout type for the display.
 */
enum class LayoutType(val rows: Int, val cols: Int, val zoneCount: Int) {
    FULL_SCREEN(1, 1, 1),
    ONE_BY_TWO(1, 2, 2),
    TWO_BY_ONE(2, 1, 2),
    TWO_BY_TWO(2, 2, 4)
}
