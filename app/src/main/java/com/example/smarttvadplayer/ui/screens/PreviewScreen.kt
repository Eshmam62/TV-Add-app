package com.example.smarttvadplayer.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarttvadplayer.domain.model.*
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.*

/**
 * Preview screen — shows the layout with configured zone content
 * without starting the timers. Allows user to verify before activating.
 */
@Composable
fun PreviewScreen(
    appConfig: AppConfig,
    onBack: () -> Unit,
    onStartPlayback: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
    ) {
        // Preview header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TvSurface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preview Layout",
                style = MaterialTheme.typography.headlineMedium,
                color = TvTextPrimary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TvButton(text = "Start Playback", onClick = onStartPlayback)
                TvButton(text = "◀ Back", onClick = onBack)
            }
        }

        // Zone grid preview
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            ZonePreviewGrid(
                layoutType = appConfig.layoutType,
                zones = appConfig.zones
            )
        }
    }
}

/**
 * Preview grid — renders zones with content preview (not live playback).
 */
@Composable
fun ZonePreviewGrid(
    layoutType: LayoutType,
    zones: List<ZoneConfig>
) {
    when (layoutType) {
        LayoutType.FULL_SCREEN -> {
            ZonePreviewContent(
                zone = zones.getOrNull(0),
                modifier = Modifier.fillMaxSize()
            )
        }
        LayoutType.ONE_BY_TWO -> {
            Row(modifier = Modifier.fillMaxSize()) {
                ZonePreviewContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
                ZonePreviewContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
            }
        }
        LayoutType.TWO_BY_ONE -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ZonePreviewContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxWidth().padding(2.dp))
                ZonePreviewContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxWidth().padding(2.dp))
            }
        }
        LayoutType.TWO_BY_TWO -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    ZonePreviewContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
                    ZonePreviewContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    ZonePreviewContent(zones.getOrNull(2), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
                    ZonePreviewContent(zones.getOrNull(3), Modifier.weight(1f).fillMaxHeight().padding(2.dp))
                }
            }
        }
    }
}

/**
 * Preview content for a single zone — shows content type label and settings summary.
 */
@Composable
fun ZonePreviewContent(
    zone: ZoneConfig?,
    modifier: Modifier = Modifier
) {
    val bgColor = when (zone?.contentType) {
        ContentType.VIDEO -> Color(0xFF1B5E20)
        ContentType.IMAGE -> Color(0xFF0D47A1)
        ContentType.TEXT -> Color(0xFF4A148C)
        ContentType.SCROLLING_TEXT -> Color(0xFFE65100)
        ContentType.COMPANY_AD -> Color(0xFF880E4F)
        else -> Color(0xFF263238)
    }

    Box(
        modifier = modifier
            .background(bgColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (zone?.contentType) {
                ContentType.VIDEO -> {
                    Text("🎬", fontSize = 32.sp)
                    Text("Video", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    if ((zone.durationMs) > 0) {
                        Text(formatDuration(zone.durationMs), color = TvTextSecondary)
                    }
                }
                ContentType.IMAGE -> {
                    Text("🖼", fontSize = 32.sp)
                    Text("Image", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    if ((zone.durationMs) > 0) {
                        Text(formatDuration(zone.durationMs), color = TvTextSecondary)
                    }
                }
                ContentType.TEXT -> {
                    val preview = zone.textContent?.take(30) ?: ""
                    Text("📝", fontSize = 32.sp)
                    Text("Text", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    if (preview.isNotEmpty()) {
                        Text("\"$preview\"", color = TvTextSecondary, maxLines = 1)
                    }
                }
                ContentType.SCROLLING_TEXT -> {
                    val preview = zone.scrollingTextSettings?.text?.take(30) ?: ""
                    Text("📜", fontSize = 32.sp)
                    Text("Scrolling Text", style = MaterialTheme.typography.titleLarge, color = Color.White)
                    if (preview.isNotEmpty()) {
                        Text("\"$preview\"", color = TvTextSecondary, maxLines = 1)
                    }
                }
                ContentType.COMPANY_AD -> {
                    Text("📺", fontSize = 32.sp)
                    Text("Company Ad", style = MaterialTheme.typography.titleLarge, color = Color.White)
                }
                else -> {
                    Text("+", style = MaterialTheme.typography.displayLarge, color = TvTextSecondary)
                    Text("Empty", color = TvTextSecondary)
                }
            }
        }
    }
}
