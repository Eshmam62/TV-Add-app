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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smarttvadplayer.domain.model.TextAlignment
import com.example.smarttvadplayer.domain.model.TextDisplaySettings
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.*

/**
 * Text editor screen — enter static text content for a zone.
 * Provides text input, size selection, color selection, alignment, and preview.
 * Designed for TV remote + on-screen keyboard input.
 */
@Composable
fun TextEditorScreen(
    zoneId: Int,
    onSave: (String, TextDisplaySettings) -> Unit,
    onCancel: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    var textSize by remember { mutableStateOf(24f) }
    var textColor by remember { mutableStateOf("#FFFFFF") }
    var backgroundColor by remember { mutableStateOf("#00000000") }
    var alignment by remember { mutableStateOf(TextAlignment.CENTER) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
            .padding(32.dp)
    ) {
        // Header
        Text(
            text = "Text Editor — Zone ${zoneId + 1}",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Text input area
        Text(
            text = "Enter text:",
            style = MaterialTheme.typography.titleMedium,
            color = TvTextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 20.sp
            ),
            cursorBrush = SolidColor(TvPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(TvSurface, RoundedCornerShape(8.dp))
                .border(2.dp, TvPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(16.dp),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text(
                            text = "Type your text here...",
                            style = TextStyle(color = TvTextSecondary, fontSize = 20.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Text size
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Size:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "Small", onClick = { textSize = 18f }, modifier = Modifier.width(120.dp))
            TvButton(text = "Medium", onClick = { textSize = 24f }, modifier = Modifier.width(120.dp))
            TvButton(text = "Large", onClick = { textSize = 36f }, modifier = Modifier.width(120.dp))
            TvButton(text = "XL", onClick = { textSize = 48f }, modifier = Modifier.width(100.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Text color
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Color:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "White", onClick = { textColor = "#FFFFFF" }, modifier = Modifier.width(120.dp))
            TvButton(text = "Yellow", onClick = { textColor = "#FFFF00" }, modifier = Modifier.width(120.dp))
            TvButton(text = "Cyan", onClick = { textColor = "#00FFFF" }, modifier = Modifier.width(120.dp))
            TvButton(text = "Green", onClick = { textColor = "#00FF00" }, modifier = Modifier.width(120.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Alignment
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Align:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "Left", onClick = { alignment = TextAlignment.LEFT }, modifier = Modifier.width(120.dp))
            TvButton(text = "Center", onClick = { alignment = TextAlignment.CENTER }, modifier = Modifier.width(120.dp))
            TvButton(text = "Right", onClick = { alignment = TextAlignment.RIGHT }, modifier = Modifier.width(120.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Background
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Background:", style = MaterialTheme.typography.titleMedium, color = TvTextSecondary)
            TvButton(text = "None", onClick = { backgroundColor = "#00000000" }, modifier = Modifier.width(120.dp))
            TvButton(text = "Black", onClick = { backgroundColor = "#FF000000" }, modifier = Modifier.width(120.dp))
            TvButton(text = "Dark", onClick = { backgroundColor = "#CC333333" }, modifier = Modifier.width(120.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(parseColor(backgroundColor), RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = when (alignment) {
                TextAlignment.LEFT -> Alignment.CenterStart
                TextAlignment.CENTER -> Alignment.Center
                TextAlignment.RIGHT -> Alignment.CenterEnd
            }
        ) {
            Text(
                text = text.ifEmpty { "Preview" },
                color = parseColor(textColor),
                fontSize = textSize.sp,
                textAlign = when (alignment) {
                    TextAlignment.LEFT -> TextAlign.Start
                    TextAlignment.CENTER -> TextAlign.Center
                    TextAlignment.RIGHT -> TextAlign.End
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                            text,
                            TextDisplaySettings(
                                textSize = textSize,
                                textColor = textColor,
                                backgroundColor = backgroundColor,
                                alignment = alignment
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

/**
 * Parse a hex color string to Compose Color.
 */
fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Color.White
    }
}
