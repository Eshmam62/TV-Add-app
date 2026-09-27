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
import com.example.smarttvadplayer.domain.model.LayoutType
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.TvBackground

/**
 * Layout selection screen — user picks Full Screen, 1×2, 2×1, or 2×2.
 */
@Composable
fun LayoutSelectionScreen(
    onSelectLayout: (LayoutType) -> Unit
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
                text = "Select Layout",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Choose how to divide your display into zones",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Row 1: Full Screen and 1x2
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvButton(
                    text = "⬜ Full Screen",
                    onClick = { onSelectLayout(LayoutType.FULL_SCREEN) },
                    modifier = Modifier.width(220.dp)
                )
                TvButton(
                    text = "◫ 1 × 2",
                    onClick = { onSelectLayout(LayoutType.ONE_BY_TWO) },
                    modifier = Modifier.width(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Row 2: 2x1 and 2x2
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvButton(
                    text = "⬒ 2 × 1",
                    onClick = { onSelectLayout(LayoutType.TWO_BY_ONE) },
                    modifier = Modifier.width(220.dp)
                )
                TvButton(
                    text = "⊞ 2 × 2",
                    onClick = { onSelectLayout(LayoutType.TWO_BY_TWO) },
                    modifier = Modifier.width(220.dp)
                )
            }
        }
    }
}
