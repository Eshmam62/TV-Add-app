package com.example.smarttvadplayer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TV-optimized color palette
val TvBackground = Color(0xFF121212)
val TvSurface = Color(0xFF1E1E1E)
val TvPrimary = Color(0xFF4FC3F7)
val TvPrimaryDark = Color(0xFF0288D1)
val TvAccent = Color(0xFF80CBC4)
val TvTextPrimary = Color(0xFFFFFFFF)
val TvTextSecondary = Color(0xB3FFFFFF)
val TvFocusBorder = Color(0xFFFFAB40)
val TvError = Color(0xFFEF5350)
val TvSurfaceVariant = Color(0xFF2C2C2C)
val TvOnSurface = Color(0xFFE0E0E0)

private val TvDarkColorScheme = darkColorScheme(
    primary = TvPrimary,
    onPrimary = Color.Black,
    secondary = TvAccent,
    onSecondary = Color.Black,
    background = TvBackground,
    onBackground = TvTextPrimary,
    surface = TvSurface,
    onSurface = TvOnSurface,
    surfaceVariant = TvSurfaceVariant,
    error = TvError,
    onError = Color.White
)

// TV-optimized typography — larger sizes for readability at distance
private val TvTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
)

/**
 * Smart TV Ad Player theme — dark, TV-optimized color scheme and typography.
 */
@Composable
fun SmartTvAdPlayerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TvDarkColorScheme,
        typography = TvTypography,
        content = content
    )
}
