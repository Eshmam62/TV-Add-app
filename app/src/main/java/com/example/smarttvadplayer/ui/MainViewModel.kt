package com.example.smarttvadplayer.ui

import android.app.Application
import android.content.pm.ActivityInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttvadplayer.data.repository.AppConfigRepository
import com.example.smarttvadplayer.domain.model.*
import com.example.smarttvadplayer.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Main ViewModel managing the entire application state.
 *
 * Handles:
 * - Navigation state
 * - Orientation selection and persistence
 * - Layout selection
 * - Zone configuration
 * - Playback state
 * - Timer management for zone expiration
 * - Company advertisement fallback
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppConfigRepository(application)

    // ========== App Config State ==========

    private val _appConfig = MutableStateFlow(AppConfig())
    val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

    // ========== Navigation State ==========

    private val _currentScreen = MutableStateFlow<String>("")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // ========== Selected Zone (for editing) ==========

    private val _selectedZoneId = MutableStateFlow(-1)
    val selectedZoneId: StateFlow<Int> = _selectedZoneId.asStateFlow()

    // ========== Orientation Request ==========

    private val _requestedOrientation = MutableStateFlow(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    val requestedOrientation: StateFlow<Int> = _requestedOrientation.asStateFlow()

    init {
        loadSavedConfig()
    }

    /**
     * Load saved configuration and determine starting screen.
     */
    private fun loadSavedConfig() {
        val config = repository.loadConfig()
        _appConfig.value = config

        if (config.isFirstRun || config.orientation.isBlank()) {
            _currentScreen.value = Screen.OrientationSelection.route
        } else {
            // Restore orientation
            applyOrientationSetting(config.orientation)
            _currentScreen.value = Screen.LayoutSelection.route
        }
    }

    // ========== Navigation ==========

    fun navigateTo(route: String) {
        _currentScreen.value = route
    }

    // ========== Orientation ==========

    fun selectOrientation(orientation: String) {
        repository.saveOrientation(orientation)
        val config = _appConfig.value.copy(
            orientation = orientation,
            isFirstRun = false
        )
        _appConfig.value = config
        applyOrientationSetting(orientation)
        _currentScreen.value = Screen.CompanyAd.route
    }

    private fun applyOrientationSetting(orientation: String) {
        _requestedOrientation.value = when (orientation.lowercase()) {
            "vertical" -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            "horizontal" -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    // ========== Company Ad ==========

    fun onCompanyAdFinished() {
        _currentScreen.value = Screen.LayoutSelection.route
    }

    fun onCompanyAdSkipped() {
        _currentScreen.value = Screen.LayoutSelection.route
    }

    // ========== Layout ==========

    fun selectLayout(layoutType: LayoutType) {
        repository.saveLayoutType(layoutType)
        val zones = AppConfig.createDefaultZones(layoutType)
        repository.saveZones(zones)
        _appConfig.value = _appConfig.value.copy(
            layoutType = layoutType,
            zones = zones
        )
        _currentScreen.value = Screen.ZoneEditor.route
    }

    // ========== Zone Editing ==========

    fun selectZone(zoneId: Int) {
        _selectedZoneId.value = zoneId
        _currentScreen.value = Screen.AddContent.createRoute(zoneId)
    }

    fun updateZoneContent(zoneId: Int, contentType: ContentType, contentUri: String? = null) {
        val zones = _appConfig.value.zones.toMutableList()
        val index = zones.indexOfFirst { it.zoneId == zoneId }
        if (index >= 0) {
            zones[index] = zones[index].copy(
                contentType = contentType,
                contentUri = contentUri
            )
            repository.saveZones(zones)
            _appConfig.value = _appConfig.value.copy(zones = zones)
        }
    }

    fun updateZoneTextContent(zoneId: Int, text: String, settings: TextDisplaySettings) {
        val zones = _appConfig.value.zones.toMutableList()
        val index = zones.indexOfFirst { it.zoneId == zoneId }
        if (index >= 0) {
            zones[index] = zones[index].copy(
                contentType = ContentType.TEXT,
                textContent = text,
                textSettings = settings
            )
            repository.saveZones(zones)
            _appConfig.value = _appConfig.value.copy(zones = zones)
        }
    }

    fun updateZoneScrollingText(zoneId: Int, settings: ScrollingTextSettings) {
        val zones = _appConfig.value.zones.toMutableList()
        val index = zones.indexOfFirst { it.zoneId == zoneId }
        if (index >= 0) {
            zones[index] = zones[index].copy(
                contentType = ContentType.SCROLLING_TEXT,
                scrollingTextSettings = settings,
                durationMs = settings.durationMs
            )
            repository.saveZones(zones)
            _appConfig.value = _appConfig.value.copy(zones = zones)
        }
    }

    fun updateZoneDuration(zoneId: Int, durationMs: Long) {
        val zones = _appConfig.value.zones.toMutableList()
        val index = zones.indexOfFirst { it.zoneId == zoneId }
        if (index >= 0) {
            zones[index] = zones[index].copy(durationMs = durationMs)
            repository.saveZones(zones)
            _appConfig.value = _appConfig.value.copy(zones = zones)
        }
    }

    // ========== Playback ==========

    fun startPlayback() {
        val zones = _appConfig.value.zones.map { zone ->
            if (zone.contentType != ContentType.EMPTY) {
                zone.withTimerStarted()
            } else {
                zone
            }
        }
        repository.saveZones(zones)
        repository.setPlaybackActive(true)
        _appConfig.value = _appConfig.value.copy(
            zones = zones,
            isPlaybackActive = true
        )
        _currentScreen.value = Screen.Playback.route
    }

    fun stopPlayback() {
        repository.setPlaybackActive(false)
        _appConfig.value = _appConfig.value.copy(isPlaybackActive = false)
        _currentScreen.value = Screen.ZoneEditor.route
    }

    /**
     * Check all zone timers and switch expired zones to company advertisement.
     * Called periodically during playback.
     */
    fun checkZoneTimers() {
        val currentTime = System.currentTimeMillis()
        var updated = false
        val zones = _appConfig.value.zones.map { zone ->
            if (!zone.isExpired && zone.hasExpired(currentTime)) {
                updated = true
                zone.withExpired()
            } else {
                zone
            }
        }
        if (updated) {
            repository.saveZones(zones)
            _appConfig.value = _appConfig.value.copy(zones = zones)
        }
    }

    // ========== Settings ==========

    fun resetAll() {
        repository.resetAll()
        _appConfig.value = AppConfig()
        _currentScreen.value = Screen.OrientationSelection.route
    }

    fun changeOrientation(orientation: String) {
        repository.saveOrientation(orientation)
        _appConfig.value = _appConfig.value.copy(orientation = orientation)
        applyOrientationSetting(orientation)
    }

    // ========== Back Navigation ==========

    /**
     * Handle BACK press — returns true if consumed, false if activity should handle it.
     */
    fun onBackPressed(): Boolean {
        val current = _currentScreen.value
        val newScreen = when {
            current == Screen.Playback.route -> {
                stopPlayback()
                return true
            }
            current == Screen.Settings.route -> Screen.ZoneEditor.route
            current == Screen.Preview.route -> Screen.ZoneEditor.route
            current.startsWith("add_content") -> Screen.ZoneEditor.route
            current.startsWith("text_editor") -> {
                val zoneId = _selectedZoneId.value
                Screen.AddContent.createRoute(zoneId)
            }
            current.startsWith("scrolling_text_editor") -> {
                val zoneId = _selectedZoneId.value
                Screen.AddContent.createRoute(zoneId)
            }
            current.startsWith("duration_picker") -> {
                val zoneId = _selectedZoneId.value
                Screen.AddContent.createRoute(zoneId)
            }
            current == Screen.ZoneEditor.route -> Screen.LayoutSelection.route
            current == Screen.LayoutSelection.route -> return false // Allow exit
            current == Screen.CompanyAd.route -> return false // Cannot go back from ad
            current == Screen.OrientationSelection.route -> return false // Allow exit
            else -> return false
        }
        _currentScreen.value = newScreen
        return true
    }
}
