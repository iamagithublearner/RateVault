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
import com.example.ratevault.ui.review.ReviewDetailScreen
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewEntry
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentDestination by remember { mutableStateOf<NavDestination>(NavDestination.Feed) }
        var showNewReview by remember { mutableStateOf(false) }
        var prefillReview by remember { mutableStateOf<Review?>(null) }
        var selectedReview by remember { mutableStateOf<Review?>(null) }
        val reviews = remember { mutableStateListOf<Review>() }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScaffold(
                currentDestination = currentDestination,
                onNavigate = { 
                    selectedReview = null
                    prefillReview = null
                    currentDestination = it 
                },
                onFabClick = { 
                    prefillReview = null
                    showNewReview = true 
                }
            ) { innerPadding ->
                // Content based on destination
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (selectedReview != null) {
                        ReviewDetailScreen(
                            review = selectedReview!!,
                            onBack = { selectedReview = null },
                            onReviewAgain = { 
                                prefillReview = selectedReview
                                showNewReview = true
                            }
                        )
                    } else {
                        when (currentDestination) {
                            NavDestination.Reviews -> {
                                ReviewsScreen(
                                    reviews = reviews,
                                    onReviewClick = { selectedReview = it }
                                )
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
        }

        if (showNewReview) {
            Dialog(
                onDismissRequest = { 
                    showNewReview = false
                    prefillReview = null
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                NewReviewScreen(
                    onDismiss = { 
                        showNewReview = false
                        prefillReview = null
                    },
                    initialName = prefillReview?.name ?: "",
                    initialCategory = prefillReview?.category,
                    onSave = { name, category, rating, notes, location, tags ->
                        val history = reviews
                            .filter { it.name == name && it.category == category }
                            .map { ReviewEntry(it.date, it.rating, it.notes) }
                            .sortedByDescending { it.date } // Basic sort, could be improved with actual date parsing

                        reviews.add(
                            Review(
                                name = name,
                                category = category,
                                rating = rating,
                                notes = notes,
                                date = getCurrentDate(),
                                location = location,
                                tags = tags,
                                previousReviews = history
                            )
                        )
                        showNewReview = false
                    }
                )
            }
        }
    }
}

