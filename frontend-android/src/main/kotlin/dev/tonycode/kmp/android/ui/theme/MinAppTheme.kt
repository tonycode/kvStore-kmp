package dev.tonycode.kmp.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat


private val DarkColorScheme = darkColorScheme(
    background = MinAppPalette.PianoBlack,
    onBackground = MinAppPalette.GhostWhite,

    primary = MinAppPalette.Purple200,
    secondary = MinAppPalette.Teal200,
    tertiary = MinAppPalette.Purple700,
)

private val LightColorScheme = lightColorScheme(
    background = MinAppPalette.GhostWhite,
    onBackground = MinAppPalette.PianoBlack,

    primary = MinAppPalette.Purple500,
    secondary = MinAppPalette.Teal200,
    tertiary = MinAppPalette.Purple700,
)


@Composable
fun MinAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+ (API 31+)
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor =
                (if (darkTheme) colorScheme.background else MinAppPalette.Teal200).toArgb()
            @Suppress("DEPRECATION")
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MinAppTypography,
        shapes = MinAppShapes,
        content = content
    )
}
