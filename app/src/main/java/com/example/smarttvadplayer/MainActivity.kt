package com.example.smarttvadplayer

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.smarttvadplayer.domain.model.ContentType
import com.example.smarttvadplayer.navigation.Screen
import com.example.smarttvadplayer.storage.StorageAccessHelper
import com.example.smarttvadplayer.ui.MainViewModel
import com.example.smarttvadplayer.ui.screens.*
import com.example.smarttvadplayer.ui.theme.SmartTvAdPlayerTheme
import com.example.smarttvadplayer.ui.theme.TvBackground

/**
 * Main activity for Smart TV Ad Player.
 *
 * Designed for Android TV with D-pad/remote navigation.
 * Handles orientation changes, SAF file picking, and complete navigation flow.
 * Compatible with API 23+.
 */
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    // Pending zone ID for content selection callback
    private var pendingContentZoneId: Int = -1
    private var pendingContentType: ContentType = ContentType.EMPTY

    // SAF Video picker
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                StorageAccessHelper.persistUriPermission(this, uri)
                viewModel.updateZoneContent(
                    pendingContentZoneId,
                    ContentType.VIDEO,
                    uri.toString()
                )
                viewModel.navigateTo(
                    Screen.DurationPicker.createRoute(pendingContentZoneId)
                )
            }
        }
    }

    // SAF Image picker
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                StorageAccessHelper.persistUriPermission(this, uri)
                viewModel.updateZoneContent(
                    pendingContentZoneId,
                    ContentType.IMAGE,
                    uri.toString()
                )
                viewModel.navigateTo(
                    Screen.DurationPicker.createRoute(pendingContentZoneId)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            SmartTvAdPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = TvBackground
                ) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }

    /**
     * Compose-based navigation host.
     */
    @Composable
    private fun AppNavigation(viewModel: MainViewModel) {
        val currentScreen by viewModel.currentScreen.collectAsState()
        val appConfig by viewModel.appConfig.collectAsState()
        val orientationValue by viewModel.requestedOrientation.collectAsState()

        // Apply orientation
        LaunchedEffect(orientationValue) {
            this@MainActivity.requestedOrientation = orientationValue
        }

        when {
            currentScreen == Screen.OrientationSelection.route -> {
                OrientationSelectionScreen(
                    onSelectOrientation = { orientation ->
                        viewModel.selectOrientation(orientation)
                    }
                )
            }

            currentScreen == Screen.CompanyAd.route -> {
                CompanyAdScreen(
                    onNext = { viewModel.onCompanyAdFinished() },
                    onSkip = { viewModel.onCompanyAdSkipped() }
                )
            }

            currentScreen == Screen.LayoutSelection.route -> {
                LayoutSelectionScreen(
                    onSelectLayout = { layoutType ->
                        viewModel.selectLayout(layoutType)
                    }
                )
            }

            currentScreen == Screen.ZoneEditor.route -> {
                ZoneEditorScreen(
                    layoutType = appConfig.layoutType,
                    zones = appConfig.zones,
                    onSelectZone = { zoneId -> viewModel.selectZone(zoneId) },
                    onPreview = { viewModel.navigateTo(Screen.Preview.route) },
                    onStartPlayback = { viewModel.startPlayback() },
                    onSettings = { viewModel.navigateTo(Screen.Settings.route) },
                    onBack = { viewModel.onBackPressed() }
                )
            }

            currentScreen.startsWith("add_content") -> {
                val zoneId = extractZoneId(currentScreen)
                AddContentScreen(
                    zoneId = zoneId,
                    onSelectVideo = {
                        pendingContentZoneId = zoneId
                        pendingContentType = ContentType.VIDEO
                        videoPickerLauncher.launch(
                            StorageAccessHelper.createVideoPickerIntent()
                        )
                    },
                    onSelectImage = {
                        pendingContentZoneId = zoneId
                        pendingContentType = ContentType.IMAGE
                        imagePickerLauncher.launch(
                            StorageAccessHelper.createImagePickerIntent()
                        )
                    },
                    onSelectText = {
                        viewModel.navigateTo(Screen.TextEditor.createRoute(zoneId))
                    },
                    onSelectScrollingText = {
                        viewModel.navigateTo(Screen.ScrollingTextEditor.createRoute(zoneId))
                    },
                    onBack = { viewModel.navigateTo(Screen.ZoneEditor.route) }
                )
            }

            currentScreen.startsWith("text_editor") -> {
                val zoneId = extractZoneId(currentScreen)
                TextEditorScreen(
                    zoneId = zoneId,
                    onSave = { text, settings ->
                        viewModel.updateZoneTextContent(zoneId, text, settings)
                        viewModel.navigateTo(
                            Screen.DurationPicker.createRoute(zoneId)
                        )
                    },
                    onCancel = {
                        viewModel.navigateTo(
                            Screen.AddContent.createRoute(zoneId)
                        )
                    }
                )
            }

            currentScreen.startsWith("scrolling_text_editor") -> {
                val zoneId = extractZoneId(currentScreen)
                ScrollingTextEditorScreen(
                    zoneId = zoneId,
                    onSave = { settings ->
                        viewModel.updateZoneScrollingText(zoneId, settings)
                        viewModel.navigateTo(Screen.ZoneEditor.route)
                    },
                    onCancel = {
                        viewModel.navigateTo(
                            Screen.AddContent.createRoute(zoneId)
                        )
                    }
                )
            }

            currentScreen.startsWith("duration_picker") -> {
                val zoneId = extractZoneId(currentScreen)
                DurationPickerScreen(
                    zoneId = zoneId,
                    onSelectDuration = { durationMs ->
                        viewModel.updateZoneDuration(zoneId, durationMs)
                        viewModel.navigateTo(Screen.ZoneEditor.route)
                    },
                    onBack = {
                        viewModel.navigateTo(
                            Screen.AddContent.createRoute(zoneId)
                        )
                    }
                )
            }

            currentScreen == Screen.Preview.route -> {
                PreviewScreen(
                    appConfig = appConfig,
                    onBack = { viewModel.navigateTo(Screen.ZoneEditor.route) },
                    onStartPlayback = { viewModel.startPlayback() }
                )
            }

            currentScreen == Screen.Playback.route -> {
                PlaybackScreen(
                    appConfig = appConfig,
                    viewModel = viewModel
                )
            }

            currentScreen == Screen.Settings.route -> {
                SettingsScreen(
                    currentOrientation = appConfig.orientation,
                    currentLayout = appConfig.layoutType.name.replace("_", " "),
                    onChangeOrientation = {
                        viewModel.navigateTo(Screen.OrientationSelection.route)
                    },
                    onResetConfig = { viewModel.resetAll() },
                    onBack = { viewModel.navigateTo(Screen.ZoneEditor.route) }
                )
            }
        }
    }

    /**
     * Extract zone ID from route strings like "add_content/2".
     */
    private fun extractZoneId(route: String): Int {
        return try {
            route.substringAfterLast("/").toInt()
        } catch (e: Exception) {
            0
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (!viewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }
}
