package com.example.si_akademik_its.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ItsLightColorScheme = lightColorScheme(
    primary             = ItsBiruTua,
    onPrimary           = ItsWhite,
    primaryContainer    = ItsBiruSurface,
    onPrimaryContainer  = ItsBiruTua,
    secondary           = ItsBiruTuaLambang,
    onSecondary         = ItsWhite,
    tertiary            = ItsKuningTua,
    onTertiary          = ItsTextPrimary,
    background          = ItsWhite,
    onBackground        = ItsTextPrimary,
    surface             = ItsWhite,
    onSurface           = ItsTextPrimary,
    surfaceVariant      = ItsSurface,
    onSurfaceVariant    = ItsTextSecondary,
    outline             = ItsBorder,
    outlineVariant      = ItsDivider,
    error               = DangerRed,
    onError             = ItsWhite
)

@Composable
fun SiakademikitsTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.WHITE
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = ItsLightColorScheme,
        content = content
    )
}
