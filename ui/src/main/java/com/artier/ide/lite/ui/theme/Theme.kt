package com.artier.ide.lite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Artier IDE Lite Dark Theme
 * Cursor IDE style color scheme based on exact VS Code Dark Modern values
 */
private val ArtierDarkColorScheme = darkColorScheme(
    // Primary - Blue accent
    primary = ArtierColors.primary,
    onPrimary = ArtierColors.onPrimary,
    primaryContainer = ArtierColors.primaryVariant,
    onPrimaryContainer = ArtierColors.onPrimary,

    // Secondary - Green success
    secondary = ArtierColors.secondary,
    onSecondary = ArtierColors.onSecondary,
    secondaryContainer = ArtierColors.secondaryVariant,
    onSecondaryContainer = ArtierColors.onSecondary,

    // Tertiary - Cyan info
    tertiary = ArtierColors.info,
    onTertiary = ArtierColors.onPrimary,
    tertiaryContainer = ArtierColors.infoVariant,
    onTertiaryContainer = ArtierColors.onPrimary,

    // Error - Red
    error = ArtierColors.error,
    onError = ArtierColors.onError,
    errorContainer = ArtierColors.errorVariant,
    onErrorContainer = ArtierColors.onError,

    // Background & Surface - Dark grays
    background = ArtierColors.background,
    onBackground = ArtierColors.onBackground,
    surface = ArtierColors.surface,
    onSurface = ArtierColors.onSurface,
    surfaceVariant = ArtierColors.surfaceVariant,
    onSurfaceVariant = ArtierColors.textMuted,
    surfaceContainer = ArtierColors.surfaceContainer,
    surfaceContainerHigh = ArtierColors.surfaceBright,
    surfaceContainerHighest = ArtierColors.surfaceHighest,

    // Outline - Borders
    outline = ArtierColors.border,
    outlineVariant = ArtierColors.divider,

    // Inverse
    inverseSurface = ArtierColors.onBackground,
    inverseOnSurface = ArtierColors.background,
    inversePrimary = ArtierColors.primaryLight,

    // Scrim
    scrim = ArtierColors.surfaceContainer,

    // Surface tint
    surfaceTint = ArtierColors.primary
)

@Composable
fun ArtierTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ArtierDarkColorScheme,
        typography = ArtierTypography,
        content = content
    )
}
