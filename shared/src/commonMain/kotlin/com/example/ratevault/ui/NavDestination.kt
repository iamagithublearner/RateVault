package com.example.ratevault.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavDestination(
    val label: String,
    val icon: ImageVector
) {
    object Feed : NavDestination("Feed", Icons.Default.Assignment)
    object Reviews : NavDestination("Reviews", Icons.Default.ChatBubble)
    object Profile : NavDestination("Profile", Icons.Default.Person)
    object Settings : NavDestination("Settings", Icons.Default.Settings)

    companion object {
        val all get() = listOf(Feed, Reviews, Profile, Settings)
    }
}
