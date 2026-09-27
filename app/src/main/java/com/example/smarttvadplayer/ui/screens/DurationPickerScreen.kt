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
 * Duration picker screen — select duration for a zone's content.
 * Options: 10s, 30s, 1m, 5m, 10m, Always (no expiration)
 */
@Composable
fun DurationPickerScreen(
    zoneId: Int,
    onSelectDuration: (Long) -> Unit,
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
                text = "Set Duration — Zone ${zoneId + 1}",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Row 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TvButton(
                    text = "10 Seconds",
                    onClick = { onSelectDuration(10_000L) },
                    modifier = Modifier.width(200.dp)
                )
                TvButton(
                    text = "30 Seconds",
                    onClick = { onSelectDuration(30_000L) },
                    modifier = Modifier.width(200.dp)
                )
                TvButton(
                    text = "1 Minute",
                    onClick = { onSelectDuration(60_000L) },
                    modifier = Modifier.width(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 2
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TvButton(
                    text = "5 Minutes",
                    onClick = { onSelectDuration(300_000L) },
                    modifier = Modifier.width(200.dp)
                )
                TvButton(
                    text = "10 Minutes",
                    onClick = { onSelectDuration(600_000L) },
                    modifier = Modifier.width(200.dp)
                )
                TvButton(
                    text = "Always",
                    onClick = { onSelectDuration(0L) },
                    modifier = Modifier.width(200.dp)
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
