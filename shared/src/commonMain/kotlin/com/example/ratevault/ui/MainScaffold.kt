package com.example.ratevault.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

class NavDestinationProvider : PreviewParameterProvider<NavDestination> {
    override val values: Sequence<NavDestination> = sequenceOf(
        NavDestination.Feed,
        NavDestination.Reviews
    )
}


@Preview
@Composable
fun MainScaffold(
    @PreviewParameter(NavDestinationProvider::class)
    currentDestination: NavDestination,

    onNavigate: (NavDestination) -> Unit = {},
    onFabClick: () -> Unit = {},
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit = {}
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
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .offset(y = 56.dp)
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
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp
    ) {
        val destinations = NavDestination.all
        
        destinations.take(2).forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination,
                onClick = { onNavigate(destination) },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Box(Modifier.size(24.dp)) },
            enabled = false,
            label = { Text("") }
        )

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
