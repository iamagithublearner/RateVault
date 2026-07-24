package com.example.ratevault

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ratevault.ui.MainScaffold
import com.example.ratevault.ui.NavDestination
import com.example.ratevault.ui.review.NewReviewScreen
import com.example.ratevault.ui.review.ReviewsScreen
import com.example.ratevault.model.Review
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentDestination by remember { mutableStateOf<NavDestination>(NavDestination.Feed) }
        var showNewReview by remember { mutableStateOf(false) }
        val reviews = remember { mutableStateListOf<Review>() }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScaffold(
                currentDestination = currentDestination,
                onNavigate = { currentDestination = it },
                onFabClick = { showNewReview = true }
            ) { innerPadding ->
                // Content based on destination
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentDestination) {
                        NavDestination.Reviews -> {
                            ReviewsScreen(reviews = reviews)
                        }
                        else -> {
                            Text(
                                text = "Current Screen: ${currentDestination.label}",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        if (showNewReview) {
            Dialog(
                onDismissRequest = { showNewReview = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                NewReviewScreen(
                    onDismiss = { showNewReview = false },
                    onSave = { name, category, rating, notes ->
                        reviews.add(Review(name, category, rating, notes))
                        showNewReview = false
                    }
                )
            }
        }
    }
}

