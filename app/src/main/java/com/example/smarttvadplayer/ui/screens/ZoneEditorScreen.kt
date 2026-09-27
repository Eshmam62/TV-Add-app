package com.example.smarttvadplayer.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smarttvadplayer.domain.model.*
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.*

/**
 * Zone Editor screen — displays the zone grid and allows selecting zones.
 * Zones are displayed in the configured grid layout (Full, 1x2, 2x1, 2x2).
 * Each zone is D-pad navigable. Empty zones show "+". Configured zones show their content type.
 */
@Composable
fun ZoneEditorScreen(
    layoutType: LayoutType,
    zones: List<ZoneConfig>,
    onSelectZone: (Int) -> Unit,
    onPreview: () -> Unit,
    onStartPlayback: () -> Unit,
    onSettings: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Zone Editor",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Layout: ${layoutType.name.replace("_", " ")}",
                style = MaterialTheme.typography.bodyLarge,
                color = TvTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zone Grid — takes most of the space
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
        ) {
            ZoneGrid(
                layoutType = layoutType,
                zones = zones,
                onSelectZone = onSelectZone
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            TvButton(text = "Preview", onClick = onPreview)
            TvButton(text = "Start Playback", onClick = onStartPlayback)
            TvButton(text = "Settings", onClick = onSettings)
            TvButton(text = "Back", onClick = onBack)
        }
    }
}

/**
 * Renders the zone grid based on layout type.
 */
@Composable
fun ZoneGrid(
    layoutType: LayoutType,
    zones: List<ZoneConfig>,
    onSelectZone: (Int) -> Unit
) {
    when (layoutType) {
        LayoutType.FULL_SCREEN -> {
            ZoneCard(
                zone = zones.getOrNull(0),
                zoneIndex = 0,
                onClick = { onSelectZone(0) },
                modifier = Modifier.fillMaxSize()
            )
        }
        LayoutType.ONE_BY_TWO -> {
            Row(modifier = Modifier.fillMaxSize()) {
                ZoneCard(
                    zone = zones.getOrNull(0),
                    zoneIndex = 0,
                    onClick = { onSelectZone(0) },
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                )
                ZoneCard(
                    zone = zones.getOrNull(1),
                    zoneIndex = 1,
                    onClick = { onSelectZone(1) },
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                )
            }
        }
        LayoutType.TWO_BY_ONE -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ZoneCard(
                    zone = zones.getOrNull(0),
                    zoneIndex = 0,
                    onClick = { onSelectZone(0) },
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(4.dp)
                )
                ZoneCard(
                    zone = zones.getOrNull(1),
                    zoneIndex = 1,
                    onClick = { onSelectZone(1) },
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(4.dp)
                )
            }
        }
        LayoutType.TWO_BY_TWO -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    ZoneCard(
                        zone = zones.getOrNull(0),
                        zoneIndex = 0,
                        onClick = { onSelectZone(0) },
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                    )
                    ZoneCard(
                        zone = zones.getOrNull(1),
                        zoneIndex = 1,
                        onClick = { onSelectZone(1) },
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                    )
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    ZoneCard(
                        zone = zones.getOrNull(2),
                        zoneIndex = 2,
                        onClick = { onSelectZone(2) },
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                    )
                    ZoneCard(
                        zone = zones.getOrNull(3),
                        zoneIndex = 3,
                        onClick = { onSelectZone(3) },
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Individual zone card — shows "+" for empty, or content type for configured zones.
 * Has clear D-pad focus state with orange border.
 */
@Composable
fun ZoneCard(
    zone: ZoneConfig?,
    zoneIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val zoneBgColors = listOf(
        Color(0xFF263238),
        Color(0xFF37474F),
        Color(0xFF455A64),
        Color(0xFF546E7A)
    )

    val bgColor = zoneBgColors.getOrElse(zoneIndex) { zoneBgColors[0] }
    val borderColor = if (isFocused) TvFocusBorder else Color(0xFF424242)
    val borderWidth = if (isFocused) 4.dp else 2.dp

    Box(
        modifier = modifier
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .background(bgColor, RoundedCornerShape(8.dp))
            .focusable(interactionSource = interactionSource)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyUp &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter)
                ) {
                    onClick()
                    true
                } else {
                    false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val contentType = zone?.contentType ?: ContentType.EMPTY

            Text(
                text = "Zone ${zoneIndex + 1}",
                style = MaterialTheme.typography.titleMedium,
                color = TvTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (contentType) {
                ContentType.EMPTY -> {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.displayLarge,
                        color = TvPrimary,
                        textAlign = TextAlign.Center
                    )
                }
                ContentType.VIDEO -> {
                    Text("🎬 Video", style = MaterialTheme.typography.titleLarge, color = TvTextPrimary)
                    zone?.durationMs?.let { dur ->
                        if (dur > 0) {
                            Text(
                                text = formatDuration(dur),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TvTextSecondary
                            )
                        }
                    }
                }
                ContentType.IMAGE -> {
                    Text("🖼 Image", style = MaterialTheme.typography.titleLarge, color = TvTextPrimary)
                    zone?.durationMs?.let { dur ->
                        if (dur > 0) {
                            Text(
                                text = formatDuration(dur),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TvTextSecondary
                            )
                        }
                    }
                }
                ContentType.TEXT -> {
                    Text("📝 Text", style = MaterialTheme.typography.titleLarge, color = TvTextPrimary)
                }
                ContentType.SCROLLING_TEXT -> {
                    Text("📜 Scrolling", style = MaterialTheme.typography.titleLarge, color = TvTextPrimary)
                }
                ContentType.COMPANY_AD -> {
                    Text("📺 Company Ad", style = MaterialTheme.typography.titleLarge, color = TvAccent)
                }
            }
        }
    }
}

/**
 * Format milliseconds to HH:MM:SS display string.
 */
fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
