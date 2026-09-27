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
 * Settings screen — change orientation, view layout, manage zones, reset.
 */
@Composable
fun SettingsScreen(
    currentOrientation: String,
    currentLayout: String,
    onChangeOrientation: () -> Unit,
    onResetConfig: () -> Unit,
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
                text = "Settings",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Info display
            Text(
                text = "Orientation: $currentOrientation",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Layout: $currentLayout",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Action buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TvButton(
                    text = "Change Orientation",
                    onClick = onChangeOrientation,
                    modifier = Modifier.width(300.dp)
                )
                TvButton(
                    text = "Reset Configuration",
                    onClick = onResetConfig,
                    modifier = Modifier.width(300.dp)
                )
                TvButton(
                    text = "About",
                    onClick = { /* About dialog - placeholder */ },
                    modifier = Modifier.width(300.dp)
                )
                TvButton(
                    text = "◀ Back",
                    onClick = onBack,
                    modifier = Modifier.width(300.dp)
                )
            }
        }
    }
}
