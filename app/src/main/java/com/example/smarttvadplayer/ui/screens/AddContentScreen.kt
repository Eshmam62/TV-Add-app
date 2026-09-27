package com.example.smarttvadplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.TvBackground

/**
 * Add Content screen — shown when user selects a zone.
 * Options: Video, Image, Text, Scrolling Text
 * All D-pad navigable.
 */
@Composable
fun AddContentScreen(
    zoneId: Int,
    onSelectVideo: () -> Unit,
    onSelectImage: () -> Unit,
    onSelectText: () -> Unit,
    onSelectScrollingText: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(48.dp)
        ) {
            Text(
                text = "Add Content — Zone ${zoneId + 1}",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select the type of content for this zone",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Content type buttons in 2x2 grid
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvButton(
                    text = "🎬 Video",
                    onClick = onSelectVideo,
                    modifier = Modifier.width(220.dp)
                )
                TvButton(
                    text = "🖼 Image",
                    onClick = onSelectImage,
                    modifier = Modifier.width(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvButton(
                    text = "📝 Text",
                    onClick = onSelectText,
                    modifier = Modifier.width(220.dp)
                )
                TvButton(
                    text = "📜 Scrolling Text",
                    onClick = onSelectScrollingText,
                    modifier = Modifier.width(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            TvButton(
                text = "◀ Back",
                onClick = onBack,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}
