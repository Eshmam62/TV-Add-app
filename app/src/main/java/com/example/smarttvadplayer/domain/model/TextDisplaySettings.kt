package com.example.smarttvadplayer.domain.model

/**
 * Display settings for text content in a zone.
 */
data class TextDisplaySettings(
    val textSize: Float = 24f,
    val textColor: String = "#FFFFFF",
    val backgroundColor: String = "#00000000",
    val alignment: TextAlignment = TextAlignment.CENTER,
    val paddingDp: Int = 8
)

enum class TextAlignment {
    LEFT, CENTER, RIGHT
}
