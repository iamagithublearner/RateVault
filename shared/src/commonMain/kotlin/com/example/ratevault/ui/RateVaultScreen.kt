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
import com.example.ratevault.ui.settings.ManageCategoriesScreen
import com.example.ratevault.ui.settings.ManageTagsScreen
import com.example.ratevault.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun RateVaultScreen(repository: ReviewRepository, backupManager: PlatformBackupManager) {
    var currentDestination by remember { mutableStateOf<NavDestination>(NavDestination.Feed) }
    var showNewReview by remember { mutableStateOf(false) }
    var prefillReview by remember { mutableStateOf<Review?>(null) }
    var selectedReview by remember { mutableStateOf<Review?>(null) }
    var showManageCategories by remember { mutableStateOf(false) }
    var showManageTags by remember { mutableStateOf(false) }
    
    val reviews by repository.getAllReviews().collectAsState(initial = emptyList())
    val categories by repository.getAllCategories().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        repository.prepopulateIfNeeded()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScaffold(
            currentDestination = currentDestination,
            onNavigate = {destination->
                selectedReview = null
                prefillReview = null
                showManageCategories = false
                showManageTags = false
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
                    val category = categories.find { it.id == selectedReview?.categoryId }
                    ReviewDetailScreen(
                        review = selectedReview!!,
                        category = category,
                        onBack = { selectedReview = null },
                        onReviewAgain = {
                            prefillReview = selectedReview
                            showNewReview = true
                        }
                    )
                } else if (showManageCategories) {
                    ManageCategoriesScreen(
                        repository = repository,
                        onBack = { showManageCategories = false }
                    )
                } else if (showManageTags) {
                    ManageTagsScreen(
                        repository = repository,
                        onBack = { showManageTags = false }
                    )
                } else {
                    when (currentDestination) {
                        NavDestination.Reviews -> {
                            ReviewsScreen(
                                reviews = reviews,
                                categories = categories,
                                onReviewClick = { selectedReview = it },
                                onDeleteReview = { review ->
                                    coroutineScope.launch {
                                        repository.deleteReview(review)
                                    }
                                }
                            )
                        }

                        NavDestination.Feed -> {
                            FeedScreen(
                                reviews = reviews,
                                categories = categories,
                                onReviewClick = { selectedReview = it }
                            )
                        }

                        NavDestination.Profile -> {

                        }

                        NavDestination.Settings -> {
                            SettingsScreen(
                                repository = repository, 
                                backupManager = backupManager,
                                onManageCategories = { showManageCategories = true },
                                onManageTags = { showManageTags = true }
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
                categories = categories,
                initialName = prefillReview?.name ?: "",
                initialCategory = categories.find { it.id == prefillReview?.categoryId },
                initialLocation = prefillReview?.location ?: "",
                initialTags = prefillReview?.tags ?: emptyList(),
                onSave = { name, category, rating, notes, location, tags ->
                    coroutineScope.launch {
                        val matchingReview = repository.getReviewByNameAndCategoryId(name, category.id)
                        
                        val history = if (matchingReview != null) {
                            (listOf(ReviewEntry(matchingReview.date, matchingReview.rating, matchingReview.notes)) + matchingReview.previousReviews)
                                .sortedByDescending { it.date }
                        } else {
                            emptyList()
                        }

                        val newReview = Review(
                            id = matchingReview?.id ?: 0,
                            name = name,
                            categoryId = category.id,
                            rating = rating,
                            notes = notes,
                            date = getCurrentDate(),
                            location = location,
                            tags = tags,
                            previousReviews = history
                        )

                        repository.saveReview(newReview)

                        if (selectedReview != null && selectedReview?.name == name && selectedReview?.categoryId == category.id) {
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
