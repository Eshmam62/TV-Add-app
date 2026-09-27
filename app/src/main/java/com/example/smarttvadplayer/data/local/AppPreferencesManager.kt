package com.example.smarttvadplayer.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.smarttvadplayer.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Local persistence using SharedPreferences.
 *
 * SharedPreferences is chosen over DataStore for API 23 compatibility and simplicity.
 * All configuration is stored as JSON strings via Gson serialization.
 */
class AppPreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("smart_tv_ad_player_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    companion object {
        private const val KEY_ORIENTATION = "orientation"
        private const val KEY_LAYOUT_TYPE = "layout_type"
        private const val KEY_ZONES = "zones_config"
        private const val KEY_IS_FIRST_RUN = "is_first_run"
        private const val KEY_IS_PLAYBACK_ACTIVE = "is_playback_active"
    }

    // ========== Orientation ==========

    fun getOrientation(): String {
        return prefs.getString(KEY_ORIENTATION, "") ?: ""
    }

    fun saveOrientation(orientation: String) {
        prefs.edit().putString(KEY_ORIENTATION, orientation).apply()
    }

    // ========== Layout Type ==========

    fun getLayoutType(): LayoutType {
        val name = prefs.getString(KEY_LAYOUT_TYPE, LayoutType.FULL_SCREEN.name)
        return try {
            LayoutType.valueOf(name ?: LayoutType.FULL_SCREEN.name)
        } catch (e: IllegalArgumentException) {
            LayoutType.FULL_SCREEN
        }
    }

    fun saveLayoutType(layoutType: LayoutType) {
        prefs.edit().putString(KEY_LAYOUT_TYPE, layoutType.name).apply()
    }

    // ========== Zone Configs ==========

    fun getZones(): List<ZoneConfig> {
        val json = prefs.getString(KEY_ZONES, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<ZoneConfig>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveZones(zones: List<ZoneConfig>) {
        val json = gson.toJson(zones)
        prefs.edit().putString(KEY_ZONES, json).apply()
    }

    // ========== First Run ==========

    fun isFirstRun(): Boolean {
        return prefs.getBoolean(KEY_IS_FIRST_RUN, true)
    }

    fun setFirstRunComplete() {
        prefs.edit().putBoolean(KEY_IS_FIRST_RUN, false).apply()
    }

    // ========== Playback State ==========

    fun isPlaybackActive(): Boolean {
        return prefs.getBoolean(KEY_IS_PLAYBACK_ACTIVE, false)
    }

    fun setPlaybackActive(active: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PLAYBACK_ACTIVE, active).apply()
    }

    // ========== Full Config ==========

    fun loadAppConfig(): AppConfig {
        return AppConfig(
            orientation = getOrientation(),
            layoutType = getLayoutType(),
            zones = getZones(),
            isFirstRun = isFirstRun(),
            isPlaybackActive = isPlaybackActive()
        )
    }

    fun saveAppConfig(config: AppConfig) {
        prefs.edit()
            .putString(KEY_ORIENTATION, config.orientation)
            .putString(KEY_LAYOUT_TYPE, config.layoutType.name)
            .putString(KEY_ZONES, gson.toJson(config.zones))
            .putBoolean(KEY_IS_FIRST_RUN, config.isFirstRun)
            .putBoolean(KEY_IS_PLAYBACK_ACTIVE, config.isPlaybackActive)
            .apply()
    }

    // ========== Reset ==========

    fun resetAll() {
        prefs.edit().clear().apply()
    }
}
