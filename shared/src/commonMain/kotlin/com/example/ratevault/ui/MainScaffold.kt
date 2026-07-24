package com.example.ratevault.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainScaffold(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit,
    onFabClick: () -> Unit,
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            RateVaultBottomBar(
                currentDestination = currentDestination,
                onNavigate = onNavigate
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onFabClick,
                containerColor = Color(0xFFF19CAF), // Custom pink from image
                contentColor = Color(0xFF4A1D2D), // Dark text/icon color
                modifier = Modifier
                    .offset(y = 56.dp) // Offset to overlap with BottomBar
                    .size(72.dp),
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = content
    )
}

@Composable
fun RateVaultBottomBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val destinations = NavDestination.all
        
        // Items 1 & 2
        destinations.take(2).forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = { onNavigate(destination) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }

        // Center Placeholder for FAB
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Box(Modifier.size(24.dp)) },
            enabled = false,
            label = { Text("") }
        )

        // Items 3 & 4
        destinations.takeLast(2).forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = { onNavigate(destination) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}
