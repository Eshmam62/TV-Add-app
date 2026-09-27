package com.example.smarttvadplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarttvadplayer.domain.model.ScrollDirection
import com.example.smarttvadplayer.domain.model.ScrollSpeed
import com.example.smarttvadplayer.domain.model.ScrollingTextSettings
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.*

/**
 * Scrolling text editor screen.
 * Configure text, color, size, background, scroll direction, speed, and duration.
 */
@Composable
fun ScrollingTextEditorScreen(
    zoneId: Int,
    onSave: (ScrollingTextSettings) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var textSize by remember { mutableStateOf(32f) }
    var textColor by remember { mutableStateOf("#FFFFFF") }
    var backgroundColor by remember { mutableStateOf("#00000000") }
    var scrollDirection by remember { mutableStateOf(ScrollDirection.RIGHT_TO_LEFT) }
    var scrollSpeed by remember { mutableStateOf(ScrollSpeed.NORMAL) }
    var durationMs by remember { mutableStateOf(0L) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
            .padding(32.dp)
    ) {
        Text(
            text = "Scrolling Text — Zone ${zoneId + 1}",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Text input
        Text("Text:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
            cursorBrush = SolidColor(TvPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(TvSurface, RoundedCornerShape(8.dp))
                .border(2.dp, TvPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(16.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text("Enter scrolling text...", style = TextStyle(color = TvTextSecondary, fontSize = 20.sp))
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Color
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Color:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "White", onClick = { textColor = "#FFFFFF" }, modifier = Modifier.width(110.dp))
            TvButton(text = "Yellow", onClick = { textColor = "#FFFF00" }, modifier = Modifier.width(110.dp))
            TvButton(text = "Red", onClick = { textColor = "#FF0000" }, modifier = Modifier.width(110.dp))
            TvButton(text = "Green", onClick = { textColor = "#00FF00" }, modifier = Modifier.width(110.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Size
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Size:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "Small", onClick = { textSize = 24f }, modifier = Modifier.width(110.dp))
            TvButton(text = "Medium", onClick = { textSize = 32f }, modifier = Modifier.width(110.dp))
            TvButton(text = "Large", onClick = { textSize = 48f }, modifier = Modifier.width(110.dp))
            TvButton(text = "XL", onClick = { textSize = 64f }, modifier = Modifier.width(100.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Direction
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Direction:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(
                text = "← Right to Left",
                onClick = { scrollDirection = ScrollDirection.RIGHT_TO_LEFT },
                modifier = Modifier.width(200.dp),
                isSelected = scrollDirection == ScrollDirection.RIGHT_TO_LEFT
            )
            TvButton(
                text = "→ Left to Right",
                onClick = { scrollDirection = ScrollDirection.LEFT_TO_RIGHT },
                modifier = Modifier.width(200.dp),
                isSelected = scrollDirection == ScrollDirection.LEFT_TO_RIGHT
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Speed
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Speed:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(
                text = "Slow",
                onClick = { scrollSpeed = ScrollSpeed.SLOW },
                modifier = Modifier.width(120.dp),
                isSelected = scrollSpeed == ScrollSpeed.SLOW
            )
            TvButton(
                text = "Normal",
                onClick = { scrollSpeed = ScrollSpeed.NORMAL },
                modifier = Modifier.width(120.dp),
                isSelected = scrollSpeed == ScrollSpeed.NORMAL
            )
            TvButton(
                text = "Fast",
                onClick = { scrollSpeed = ScrollSpeed.FAST },
                modifier = Modifier.width(120.dp),
                isSelected = scrollSpeed == ScrollSpeed.FAST
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Duration
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Duration:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "1 min", onClick = { durationMs = 60_000L }, modifier = Modifier.width(110.dp))
            TvButton(text = "5 min", onClick = { durationMs = 300_000L }, modifier = Modifier.width(110.dp))
            TvButton(text = "10 min", onClick = { durationMs = 600_000L }, modifier = Modifier.width(110.dp))
            TvButton(text = "Always", onClick = { durationMs = 0L }, modifier = Modifier.width(110.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save / Cancel
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            TvButton(
                text = "Save",
                onClick = {
                    if (text.isNotBlank()) {
                        onSave(
                            ScrollingTextSettings(
                                text = text,
                                textSize = textSize,
                                textColor = textColor,
                                backgroundColor = backgroundColor,
                                scrollDirection = scrollDirection,
                                scrollSpeed = scrollSpeed,
                                durationMs = durationMs
                            )
                        )
                    }
                },
                modifier = Modifier.width(160.dp)
            )
            TvButton(
                text = "Cancel",
                onClick = onCancel,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}
