package com.clickpost.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

// ── Brand Colours ─────────────────────────────────────────────────────────────
val Teal500   = Color(0xFF4FA3B8)
val Teal400   = Color(0xFF6EC6D9)
val Teal300   = Color(0xFF9DD8E8)
val Teal900   = Color(0xFF0D4F5E)

val Grey50    = Color(0xFFF5F6F8)
val Grey100   = Color(0xFFE8EAED)
val Grey600   = Color(0xFF6B6E7A)
val Grey900   = Color(0xFF1C1C1E)

val White     = Color(0xFFFFFFFF)
val Error     = Color(0xFFE53935)
val Success   = Color(0xFF43A047)

// ── Colour Schemes ─────────────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary          = Teal500,
    onPrimary        = White,
    primaryContainer = Teal300,
    onPrimaryContainer = Teal900,
    secondary        = Teal400,
    onSecondary      = White,
    background       = Grey50,
    onBackground     = Grey900,
    surface          = White,
    onSurface        = Grey900,
    surfaceVariant   = Grey100,
    onSurfaceVariant = Grey600,
    error            = Error
)

private val DarkColors = darkColorScheme(
    primary          = Teal400,
    onPrimary        = Teal900,
    primaryContainer = Teal900,
    onPrimaryContainer = Teal300,
    secondary        = Teal500,
    onSecondary      = Teal900,
    background       = Color(0xFF121212),
    onBackground     = Grey100,
    surface          = Color(0xFF1E1E1E),
    onSurface        = Grey100,
    surfaceVariant   = Color(0xFF2A2A2A),
    onSurfaceVariant = Grey600,
    error            = Color(0xFFEF9A9A)
)

// ── Typography ────────────────────────────────────────────────────────────────
val ClickPostTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)

// ── Theme Composable ──────────────────────────────────────────────────────────
@Composable
fun ClickPostTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = ClickPostTypography,
        content = content
    )
}
