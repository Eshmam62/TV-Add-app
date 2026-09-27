package com.example.smarttvadplayer.domain.model

/**
 * Configuration for a single zone in the display layout.
 *
 * Each zone independently manages its own content, duration timer, and playback state.
 * When a zone's duration expires, it automatically switches to COMPANY_AD content.
 */
data class ZoneConfig(
    val zoneId: Int = 0,
    val layoutPosition: Int = 0,
    val contentType: ContentType = ContentType.EMPTY,
    val contentUri: String? = null,
    val textContent: String? = null,
    val textSettings: TextDisplaySettings? = null,
    val scrollingTextSettings: ScrollingTextSettings? = null,
    val durationMs: Long = 0L,
    val startTimeMs: Long = 0L,
    val expirationTimeMs: Long = 0L,
    val isExpired: Boolean = false
) {
    /**
     * Calculate remaining time in milliseconds.
     * Returns 0 if already expired or no duration set.
     */
    fun remainingTimeMs(currentTimeMs: Long = System.currentTimeMillis()): Long {
        if (durationMs <= 0L) return Long.MAX_VALUE // No expiration
        if (expirationTimeMs <= 0L) return durationMs
        val remaining = expirationTimeMs - currentTimeMs
        return if (remaining > 0) remaining else 0L
    }

    /**
     * Check if this zone's content duration has expired.
     */
    fun hasExpired(currentTimeMs: Long = System.currentTimeMillis()): Boolean {
        if (durationMs <= 0L) return false // Duration 0 = no expiration
        if (expirationTimeMs <= 0L) return false
        return currentTimeMs >= expirationTimeMs
    }

    /**
     * Start the timer for this zone and compute the expiration time.
     */
    fun withTimerStarted(currentTimeMs: Long = System.currentTimeMillis()): ZoneConfig {
        if (durationMs <= 0L) return this // No expiration needed
        return copy(
            startTimeMs = currentTimeMs,
            expirationTimeMs = currentTimeMs + durationMs,
            isExpired = false
        )
    }

    /**
     * Mark this zone as expired and switch to company ad.
     */
    fun withExpired(): ZoneConfig {
        return copy(
            contentType = ContentType.COMPANY_AD,
            isExpired = true
        )
    }
}
