package com.artier.ide.lite.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Artier IDE Lite - Cursor IDE Style Dark Theme Colors
 *
 * Based on exact Cursor IDE / VS Code Dark Modern theme palette
 * Source: VS Code Dark Modern theme (dark_modern.json)
 */
object ArtierColors {
    // Backgrounds (darkest to lightest)
    val background = Color(0xFF1F1F1F)              // Editor background (#1F1F1F)
    val surface = Color(0xFF181818)                 // Sidebar, panels (#181818)
    val surfaceVariant = Color(0xFF2B2B2B)          // Tab bar, status bar (#2B2B2B)
    val surfaceContainer = Color(0xFF202020)        // Editor widget (#202020)
    val surfaceBright = Color(0xFF313131)           // Dropdown, input (#313131)
    val surfaceHighest = Color(0xFF3C3C3C)          // Borders, dividers (#3C3C3C)

    // Text (brightest to darkest)
    val onBackground = Color(0xFFCCCCCC)            // Primary text (#CCCCCC)
    val onSurface = Color(0xFFD7D7D7)               // Secondary text (#D7D7D7)
    val textMuted = Color(0xFF9D9D9D)               // Muted text (#9D9D9D)
    val textDisabled = Color(0xFF6E7681)            // Line numbers (#6E7681)
    val textPlaceholder = Color(0xFF989898)         // Input placeholder (#989898)

    // Accent - Primary Blue (Cursor-style)
    val primary = Color(0xFF0078D4)                 // Primary accent (#0078D4)
    val primaryVariant = Color(0xFF026EC1)          // Hover blue (#026EC1)
    val primaryLight = Color(0xFF2488DB)            // Input active (#2488DB)
    val onPrimary = Color(0xFFFFFFFF)               // Text on primary

    // Secondary - Green (Accept/Success)
    val secondary = Color(0xFF2EA043)               // Success green (#2EA043)
    val secondaryVariant = Color(0xFF1A7F37)        // Darker green
    val onSecondary = Color(0xFFFFFFFF)             // Text on secondary

    // Error - Red (Reject/Error)
    val error = Color(0xFFF85149)                   // Error red (#F85149)
    val errorVariant = Color(0xFFDA3633)            // Darker red
    val onError = Color(0xFFFFFFFF)                 // Text on error

    // Warning - Orange
    val warning = Color(0xFFCCA700)                 // Warning yellow/orange
    val warningVariant = Color(0xFFB89400)          // Darker warning

    // Info - Cyan
    val info = Color(0xFF4EC9B0)                    // Info cyan (types color)
    val infoVariant = Color(0xFF3DC9B0)             // Darker cyan

    // Diff colors (semi-transparent overlays)
    val diffAdded = Color(0x3300FF00)               // Green overlay, 20% opacity
    val diffRemoved = Color(0x33FF0000)             // Red overlay, 20% opacity
    val diffAddedText = Color(0xFF7EE787)           // Green text for additions
    val diffRemovedText = Color(0xFFFFA198)         // Red text for removals
    val diffBorder = Color(0xFF6E7681)              // Diff border color

    // Ghost text (Autocomplete) - ~40% opacity of foreground
    val ghostText = Color(0x66CCCCCC)               // Semi-transparent foreground
    val ghostTextBackground = Color(0x1A6A9955)     // Very subtle green background

    // Syntax highlighting (VS Code Dark+ / Dark Modern)
    val keyword = Color(0xFF569CD6)                 // Blue keywords
    val string = Color(0xFFCE9178)                  // Orange strings
    val comment = Color(0xFF6A9955)                 // Green comments
    val function = Color(0xFFDCDCAA)                // Yellow functions
    val type = Color(0xFF4EC9B0)                    // Cyan types
    val variable = Color(0xFF9CDCFE)                // Light blue variables
    val number = Color(0xFFB5CEA8)                  // Green numbers
    val operator = Color(0xFFD4D4D4)                // White operators
    val annotation = Color(0xFFD7BA7D)              // Yellow annotations
    val constant = Color(0xFF4FC1FF)                // Light blue constants
    val controlFlow = Color(0xFFC586C0)             // Purple control flow
    val escapeChar = Color(0xFFD7BA7D)              // Gold escape characters

    // UI Element colors
    val border = Color(0xFF3C3C3C)                  // General borders (#3C3C3C)
    val borderFocus = Color(0xFF0078D4)             // Focused border
    val divider = Color(0xFF2B2B2B)                 // Panel borders (#2B2B2B)
    val inputBackground = Color(0xFF313131)         // Input field background (#313131)
    val inputBorder = Color(0xFF3C3C3C)             // Input field border (#3C3C3C)

    // Scrollbar
    val scrollbarThumb = Color(0x42FFFFFF)          // Scrollbar thumb
    val scrollbarTrack = Color(0x00000000)          // Scrollbar track (transparent)

    // Selection
    val selection = Color(0x330078D4)               // Text selection highlight
    val selectionBackground = Color(0x264F78)       // Selection background

    // Tab states
    val tabActive = Color(0xFF1F1F1F)               // Active tab background
    val tabInactive = Color(0xFF181818)             // Inactive tab background
    val tabActiveBorderTop = Color(0xFF0078D4)      // Active tab top border

    // Status bar
    val statusBarDebugging = Color(0xFF0078D4)      // Debugging status bar
    val statusBarRemote = Color(0xFF0078D4)         // Remote status bar
}
