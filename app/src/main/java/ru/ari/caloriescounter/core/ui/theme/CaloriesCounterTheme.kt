package ru.ari.caloriescounter.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import ru.ari.caloriescounter.R

private val LightColors = lightColorScheme(
    primary = Color(0xFF4E8B6A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6ECDD),
    secondary = Color(0xFF6F7D75),
    background = Color(0xFFF7FAF8),
    surface = Color(0xFFFFFFFF),
    error = Color(0xFFB3261E),
    onBackground = Color(0xFF1C1F1D),
    onSurface = Color(0xFF1C1F1D),
    onSurfaceVariant = Color(0xFF5E6660),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7BC79B),
    onPrimary = Color(0xFF1A1F1C),
    primaryContainer = Color(0xFF345643),
    secondary = Color(0xFFA8B7AE),
    background = Color(0xFF121614),
    surface = Color(0xFF1A1F1C),
    error = Color(0xFFFFB4AB),
    onBackground = Color(0xFFE5ECE8),
    onSurface = Color(0xFFE5ECE8),
    onSurfaceVariant = Color(0xFFB2BCB5),
)

private val Manrope = FontFamily(Font(R.font.manrope_variable, FontWeight.SemiBold))
private val Inter = FontFamily(Font(R.font.inter_variable))

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun CaloriesCounterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
