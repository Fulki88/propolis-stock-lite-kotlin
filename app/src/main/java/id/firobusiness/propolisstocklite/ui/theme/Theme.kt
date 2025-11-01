package id.firobusiness.propolisstocklite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Custom color palette
private val PrimaryDarkRed = Color(0xFF780000)
private val PrimaryRed = Color(0xFFc1121f)
private val PrimaryCream = Color(0xFFfdf0d5)
private val PrimaryDarkBlue = Color(0xFF003049)
private val PrimaryLightBlue = Color(0xFF669bbc)

// Light theme colors
private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = PrimaryCream,
    primaryContainer = PrimaryDarkRed,
    onPrimaryContainer = PrimaryCream,
    secondary = PrimaryLightBlue,
    onSecondary = PrimaryCream,
    secondaryContainer = PrimaryDarkBlue,
    onSecondaryContainer = PrimaryCream,
    tertiary = PrimaryDarkBlue,
    onTertiary = PrimaryCream,
    background = PrimaryCream,
    onBackground = PrimaryDarkRed,
    surface = PrimaryCream,
    onSurface = PrimaryDarkRed,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = PrimaryDarkBlue,
    outline = PrimaryLightBlue,
    outlineVariant = Color(0xFFE0E0E0)
)

// Dark theme colors
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightBlue,
    onPrimary = PrimaryDarkBlue,
    primaryContainer = PrimaryRed,
    onPrimaryContainer = PrimaryCream,
    secondary = PrimaryRed,
    onSecondary = PrimaryCream,
    secondaryContainer = PrimaryDarkRed,
    onSecondaryContainer = PrimaryCream,
    tertiary = PrimaryCream,
    onTertiary = PrimaryDarkBlue,
    background = PrimaryDarkBlue,
    onBackground = PrimaryCream,
    surface = PrimaryDarkBlue,
    onSurface = PrimaryCream,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = PrimaryLightBlue,
    outline = PrimaryRed,
    outlineVariant = Color(0xFF404040)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
