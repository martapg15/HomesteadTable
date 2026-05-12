package dam_a51564.homesteadtable.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = White,
    primaryContainer = TerracottaLight,
    onPrimaryContainer = Espresso,

    secondary = BurntRed,
    onSecondary = White,
    secondaryContainer = BurntRedLight,
    onSecondaryContainer = Espresso,

    tertiary = WarmTan,
    onTertiary = White,
    tertiaryContainer = TerracottaMid,
    onTertiaryContainer = Espresso,

    background = Cream,
    onBackground = Espresso,

    surface = White,
    onSurface = Espresso,
    surfaceVariant = TerracottaLight,
    onSurfaceVariant = WarmTan,

    outline = ParchmentBorder,
    outlineVariant = ParchmentBorder2,

    error = BurntRed,
    onError = White,
    errorContainer = BurntRedLight,
    onErrorContainer = Espresso,
)

private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = Espresso,
    primaryContainer = TerracottaDark,
    onPrimaryContainer = TerracottaLight,

    secondary = BurntRed,
    onSecondary = Espresso,
    secondaryContainer = BurntRedLight,
    onSecondaryContainer = Espresso,

    tertiary = LightTan,
    onTertiary = Espresso,
    tertiaryContainer = TerracottaDark,
    onTertiaryContainer = TerracottaLight,

    background = Espresso,
    onBackground = Cream,

    surface = Espresso,
    onSurface = Cream,
    surfaceVariant = LigtherBrown,
    onSurfaceVariant = LightTan,

    outline = TerracottaDark,
    outlineVariant = WarmTan,
)

@Composable
fun HomesteadTableTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}