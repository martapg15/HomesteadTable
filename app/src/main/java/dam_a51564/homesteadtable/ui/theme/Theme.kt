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

    background = Cream,
    onBackground = Espresso,

    surface = White,
    onSurface = Espresso,
    onSurfaceVariant = WarmTan,

    outline = ParchmentBorder,
    inverseOnSurface = Cream
)

private val DarkColorScheme = darkColorScheme(
    primary = Terracotta,
    onPrimary = Espresso,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = Cream,

    secondary = BurntRed,
    onSecondary = Espresso,

    background = DarkBackground,
    onBackground = Cream,

    surface = DarkSurface,
    onSurface = Cream,
    onSurfaceVariant = LightTan,

    outline = LigtherBrown,
    inverseOnSurface = DarkBackground
)

@Composable
fun HomesteadTableTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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