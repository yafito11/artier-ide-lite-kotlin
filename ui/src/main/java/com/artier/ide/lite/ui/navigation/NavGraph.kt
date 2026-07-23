package com.artier.ide.lite.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artier.ide.lite.ui.components.MainWorkspace

/**
 * Main navigation graph for Artier IDE Lite
 */
@Composable
fun ArtierNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Workspace.route
    ) {
        composable(Screen.Workspace.route) {
            MainWorkspace()
        }

        composable(Screen.Settings.route) {
            // SettingsScreen()
        }
    }
}

/**
 * Screen routes
 */
sealed class Screen(val route: String) {
    data object Workspace : Screen("workspace")
    data object Settings : Screen("settings")
}
