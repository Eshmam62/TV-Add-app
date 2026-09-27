package com.example.smarttvadplayer.ui.screens

import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarttvadplayer.domain.model.*
import com.example.smarttvadplayer.media.player.CompanyAdProvider
import com.example.smarttvadplayer.media.player.ZoneVideoPlayer
import com.example.smarttvadplayer.storage.StorageAccessHelper
import com.example.smarttvadplayer.ui.MainViewModel
import com.example.smarttvadplayer.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Playback screen — the main digital signage display.
 *
 * Renders all zones simultaneously with independent content and timers.
 * When a zone's timer expires, it automatically switches to company advertisement.
 * All controls are hidden during playback.
 */
@Composable
fun PlaybackScreen(
    appConfig: AppConfig,
    viewModel: MainViewModel
) {
    // Periodically check zone timers
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Check every second
            viewModel.checkZoneTimers()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        PlaybackZoneGrid(
            layoutType = appConfig.layoutType,
            zones = appConfig.zones
        )
    }
}

/**
 * Renders the playback zone grid — same layout as ZoneGrid but with live content.
 */
@Composable
fun PlaybackZoneGrid(
    layoutType: LayoutType,
    zones: List<ZoneConfig>
) {
    when (layoutType) {
        LayoutType.FULL_SCREEN -> {
            PlaybackZoneContent(
                zone = zones.getOrNull(0),
                modifier = Modifier.fillMaxSize()
            )
        }
        LayoutType.ONE_BY_TWO -> {
            Row(modifier = Modifier.fillMaxSize()) {
                PlaybackZoneContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxHeight())
                PlaybackZoneContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxHeight())
            }
        }
        LayoutType.TWO_BY_ONE -> {
            Column(modifier = Modifier.fillMaxSize()) {
                PlaybackZoneContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxWidth())
                PlaybackZoneContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxWidth())
            }
        }
        LayoutType.TWO_BY_TWO -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    PlaybackZoneContent(zones.getOrNull(0), Modifier.weight(1f).fillMaxHeight())
                    PlaybackZoneContent(zones.getOrNull(1), Modifier.weight(1f).fillMaxHeight())
                }
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    PlaybackZoneContent(zones.getOrNull(2), Modifier.weight(1f).fillMaxHeight())
                    PlaybackZoneContent(zones.getOrNull(3), Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
    }
}

/**
 * Renders the live content for a single playback zone.
 */
@Composable
fun PlaybackZoneContent(
    zone: ZoneConfig?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clipToBounds()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (zone?.contentType) {
            ContentType.VIDEO -> {
                val uri = zone.contentUri
                if (uri != null && StorageAccessHelper.isUriAccessible(context, uri)) {
                    ZoneVideoPlayer(
                        uri = Uri.parse(uri),
                        modifier = Modifier.fillMaxSize(),
                        loop = true
                    )
                } else {
                    ContentUnavailableMessage()
                }
            }

            ContentType.IMAGE -> {
                val uri = zone.contentUri
                if (uri != null) {
                    // Use a simple approach for image display without Coil
                    ImageDisplay(uri = uri, modifier = Modifier.fillMaxSize())
                } else {
                    ContentUnavailableMessage()
                }
            }

            ContentType.TEXT -> {
                val settings = zone.textSettings ?: TextDisplaySettings()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(parseColor(settings.backgroundColor))
                        .padding(settings.paddingDp.dp),
                    contentAlignment = when (settings.alignment) {
                        TextAlignment.LEFT -> Alignment.CenterStart
                        TextAlignment.CENTER -> Alignment.Center
                        TextAlignment.RIGHT -> Alignment.CenterEnd
                    }
                ) {
                    Text(
                        text = zone.textContent ?: "",
                        color = parseColor(settings.textColor),
                        fontSize = settings.textSize.sp,
                        textAlign = when (settings.alignment) {
                            TextAlignment.LEFT -> TextAlign.Start
                            TextAlignment.CENTER -> TextAlign.Center
                            TextAlignment.RIGHT -> TextAlign.End
                        }
                    )
                }
            }

            ContentType.SCROLLING_TEXT -> {
                val settings = zone.scrollingTextSettings ?: ScrollingTextSettings()
                ScrollingTextDisplay(
                    settings = settings,
                    modifier = Modifier.fillMaxSize()
                )
            }

            ContentType.COMPANY_AD -> {
                val adUri = CompanyAdProvider.getCompanyAdUri(context)
                if (CompanyAdProvider.isCompanyAdAvailable(context)) {
                    ZoneVideoPlayer(
                        uri = adUri,
                        modifier = Modifier.fillMaxSize(),
                        loop = true
                    )
                } else {
                    // Fallback when no company ad video exists
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF880E4F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Company\nAdvertisement",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Content",
                        color = TvTextSecondary,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

/**
 * Image display using Android's BitmapFactory — no Coil dependency needed.
 * Works on API 23+.
 */
@Composable
fun ImageDisplay(uri: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val bitmap = remember(uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
            val bmp = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bmp
        } catch (e: Exception) {
            null
        }
    }

    if (bitmap != null) {
        val imageBitmap = remember(bitmap) {
            bitmap.asImageBitmap()
        }
        Image(
            bitmap = imageBitmap,
            contentDescription = "Zone image",
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        ContentUnavailableMessage()
    }
}

/**
 * Scrolling text display with smooth animation.
 * Text scrolls horizontally within the zone bounds.
 */
@Composable
fun ScrollingTextDisplay(
    settings: ScrollingTextSettings,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    // Infinite scrolling animation
    val infiniteTransition = rememberInfiniteTransition(label = "scroll")

    // Animation duration based on speed — faster = shorter duration
    val animDurationMs = when (settings.scrollSpeed) {
        ScrollSpeed.SLOW -> 12000
        ScrollSpeed.NORMAL -> 7000
        ScrollSpeed.FAST -> 3500
    }

    val offsetAnimation by infiniteTransition.animateFloat(
        initialValue = if (settings.scrollDirection == ScrollDirection.RIGHT_TO_LEFT) 1f else -1f,
        targetValue = if (settings.scrollDirection == ScrollDirection.RIGHT_TO_LEFT) -1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animDurationMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "scrollOffset"
    )

    Box(
        modifier = modifier
            .clipToBounds()
            .background(parseColor(settings.backgroundColor)),
        contentAlignment = Alignment.CenterStart
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val containerWidthPx = with(density) { maxWidth.toPx() }

            Text(
                text = settings.text,
                color = parseColor(settings.textColor),
                fontSize = settings.textSize.sp,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (offsetAnimation * containerWidthPx).toInt(),
                            y = 0
                        )
                    }
                    .wrapContentWidth(unbounded = true)
            )
        }
    }
}

/**
 * "Content unavailable" message for missing/deleted media.
 */
@Composable
fun ContentUnavailableMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF37474F)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Content is unavailable.",
                style = MaterialTheme.typography.titleLarge,
                color = TvError,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The media file may have been removed.",
                style = MaterialTheme.typography.bodyMedium,
                color = TvTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
