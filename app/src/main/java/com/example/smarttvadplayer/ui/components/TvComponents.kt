package com.example.smarttvadplayer.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.example.smarttvadplayer.ui.theme.TvFocusBorder
import com.example.smarttvadplayer.ui.theme.TvPrimary
import com.example.smarttvadplayer.ui.theme.TvSurface

/**
 * TV-friendly button with clear D-pad focus states.
 * Shows a prominent orange border when focused via D-pad.
 */
@Composable
fun TvButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        isFocused -> TvFocusBorder
        isSelected -> TvPrimary
        else -> Color.Transparent
    }

    val containerColor = when {
        isFocused -> TvPrimary
        isSelected -> TvPrimary.copy(alpha = 0.3f)
        else -> TvSurface
    }

    val textColor = when {
        isFocused -> Color.Black
        else -> Color.White
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(64.dp)
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
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = textColor
        ),
        border = BorderStroke(3.dp, borderColor),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}

/**
 * TV-friendly card with D-pad focus states.
 * Used for zone cards and option cards.
 */
@Composable
fun TvCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isFocused: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hasFocus by interactionSource.collectIsFocusedAsState()
    val focused = isFocused || hasFocus

    val borderColor = if (focused) TvFocusBorder else Color.Transparent

    Card(
        modifier = modifier
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (focused) TvSurface.copy(alpha = 0.9f) else TvSurface
        ),
        border = BorderStroke(if (focused) 3.dp else 1.dp, borderColor)
    ) {
        content()
    }
}
