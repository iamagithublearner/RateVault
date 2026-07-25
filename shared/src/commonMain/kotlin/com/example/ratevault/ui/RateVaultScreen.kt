package com.example.ratevault.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ratevault.getCurrentDate
import com.example.ratevault.data.ReviewRepository
import com.example.ratevault.data.PlatformBackupManager
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewEntry
import com.example.ratevault.ui.feed.FeedScreen
import com.example.ratevault.ui.review.NewReviewScreen
import com.example.ratevault.ui.review.ReviewDetailScreen
import com.example.ratevault.ui.review.ReviewsScreen
import com.example.ratevault.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun RateVaultScreen(repository: ReviewRepository, backupManager: PlatformBackupManager) {
    var currentDestination by remember { mutableStateOf<NavDestination>(NavDestination.Feed) }
    var showNewReview by remember { mutableStateOf(false) }
    var prefillReview by remember { mutableStateOf<Review?>(null) }
    var selectedReview by remember { mutableStateOf<Review?>(null) }
    
    val reviews by repository.getAllReviews().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

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

                        NavDestination.Feed -> {
                            FeedScreen(
                                reviews = reviews,
                                onReviewClick = { selectedReview = it }
                            )
                        }

                        NavDestination.Profile -> {

                        }

                        NavDestination.Settings -> {
                            SettingsScreen(repository, backupManager)
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
                    coroutineScope.launch {
                        val matchingReview = repository.getReviewByNameAndCategory(name, category.name)
                        
                        val history = if (matchingReview != null) {
                            (listOf(ReviewEntry(matchingReview.date, matchingReview.rating, matchingReview.notes)) + matchingReview.previousReviews)
                                .sortedByDescending { it.date }
                        } else {
                            emptyList()
                        }

                        val newReview = Review(
                            id = matchingReview?.id ?: 0,
                            name = name,
                            category = category,
                            rating = rating,
                            notes = notes,
                            date = getCurrentDate(),
                            location = location,
                            tags = tags,
                            previousReviews = history
                        )

                        repository.saveReview(newReview)

                        if (selectedReview != null && selectedReview?.name == name && selectedReview?.category == category) {
                            selectedReview = newReview
                        }

                        showNewReview = false
                        prefillReview = null
                    }
                }
            )
        }
    }
}
