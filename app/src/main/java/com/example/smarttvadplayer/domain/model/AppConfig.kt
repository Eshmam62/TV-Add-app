package com.example.smarttvadplayer.domain.model

/**
 * Complete application state model.
 * Persisted locally via DataStore/SharedPreferences.
 */
data class AppConfig(
    val orientation: String = "",           // "" = not yet selected (first run)
    val layoutType: LayoutType = LayoutType.FULL_SCREEN,
    val zones: List<ZoneConfig> = emptyList(),
    val isFirstRun: Boolean = true,
    val isPlaybackActive: Boolean = false
) {
    companion object {
        /**
         * Create default zone configs for a given layout type.
         */
        fun createDefaultZones(layoutType: LayoutType): List<ZoneConfig> {
            return (0 until layoutType.zoneCount).map { index ->
                ZoneConfig(
                    zoneId = index,
                    layoutPosition = index,
                    contentType = ContentType.EMPTY
                )
            }
        }
    }
}
