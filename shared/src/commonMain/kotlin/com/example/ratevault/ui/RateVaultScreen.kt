package com.example.ratevault.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ratevault.getCurrentDate
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewEntry
import com.example.ratevault.ui.review.NewReviewScreen
import com.example.ratevault.ui.review.ReviewDetailScreen
import com.example.ratevault.ui.review.ReviewsScreen

@Preview
@Composable
fun RateVaultScreen() {
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
            onNavigate = {destination->
                selectedReview = null
                prefillReview = null
                currentDestination = destination
            },
            onFabClick = {
                prefillReview = null
                showNewReview = true
            }
        ) { innerPadding ->
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
                initialLocation = prefillReview?.location ?: "",
                initialTags = prefillReview?.tags ?: emptyList(),
                onSave = { name, category, rating, notes, location, tags ->
                    val matchingReviews = reviews.filter { it.name == name && it.category == category }

                    val history = matchingReviews
                        .flatMap {
                            listOf(ReviewEntry(it.date, it.rating, it.notes)) + it.previousReviews
                        }
                        .sortedByDescending { it.date }

                    val newReview = Review(
                        name = name,
                        category = category,
                        rating = rating,
                        notes = notes,
                        date = getCurrentDate(),
                        location = location,
                        tags = tags,
                        previousReviews = history
                    )

                    reviews.removeAll(matchingReviews)
                    reviews.add(0, newReview)

                    if (selectedReview != null && selectedReview?.name == name && selectedReview?.category == category) {
                        selectedReview = newReview
                    }

                    showNewReview = false
                    prefillReview = null
                }
            )
        }
    }
}
