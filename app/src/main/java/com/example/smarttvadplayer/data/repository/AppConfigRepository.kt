package com.example.smarttvadplayer.data.repository

import android.content.Context
import com.example.smarttvadplayer.data.local.AppPreferencesManager
import com.example.smarttvadplayer.domain.model.*

/**
 * Repository providing a clean interface to application configuration.
 * Bridges domain layer with persistence layer.
 */
class AppConfigRepository(context: Context) {

    private val prefs = AppPreferencesManager(context)

    fun loadConfig(): AppConfig {
        return prefs.loadAppConfig()
    }

    fun saveConfig(config: AppConfig) {
        prefs.saveAppConfig(config)
    }

    fun getOrientation(): String = prefs.getOrientation()

    fun saveOrientation(orientation: String) {
        prefs.saveOrientation(orientation)
        prefs.setFirstRunComplete()
    }

    fun getLayoutType(): LayoutType = prefs.getLayoutType()

    fun saveLayoutType(layoutType: LayoutType) {
        prefs.saveLayoutType(layoutType)
    }

    fun getZones(): List<ZoneConfig> = prefs.getZones()

    fun saveZones(zones: List<ZoneConfig>) {
        prefs.saveZones(zones)
    }

    fun isFirstRun(): Boolean = prefs.isFirstRun()

    fun setPlaybackActive(active: Boolean) {
        prefs.setPlaybackActive(active)
    }

    fun resetAll() {
        prefs.resetAll()
    }
}
