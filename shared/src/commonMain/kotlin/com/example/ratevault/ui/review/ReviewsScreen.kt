package com.example.ratevault.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.model.Category
import com.example.ratevault.model.Review
import com.example.ratevault.ui.components.RateVaultTopAppBar
import com.example.ratevault.ui.utils.IconUtils

enum class DragAnchors {
    Closed, Open
}

@Composable
fun ReviewsScreen(
    reviews: List<Review>,
    categories: List<Category>,
    onReviewClick: (Review) -> Unit = {},
    onDeleteReview: (Review) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F8))
    ) {
        RateVaultTopAppBar(title = "Your Reviews")

        if (reviews.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No reviews yet. Tap + to add one!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reviews, key = { it.id }) { review ->
                    val category = categories.find { it.id == review.categoryId }
                    val density = LocalDensity.current
                    val openAnchor = with(density) { -80.dp.toPx() }
                    
                    val anchors = remember {
                        DraggableAnchors {
                            DragAnchors.Closed at 0f
                            DragAnchors.Open at openAnchor
                        }
                    }
                    
                    val state = remember {
                        AnchoredDraggableState(
                            initialValue = DragAnchors.Closed,
                            anchors = anchors
                        )
                    }
                    
                    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                        state = state,
                        positionalThreshold = { distance: Float -> distance * 0.5f },
                        animationSpec = androidx.compose.animation.core.tween<Float>()
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(horizontal = 16.dp)
                    ) {
                        // Background (Delete Button)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Alignment.CenterEnd)
                                .width(80.dp)
                                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                .background(Color(0xFF703E4B))
                                .clickable { onDeleteReview(review) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Content (Review Item)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset {
                                    IntOffset(state.requireOffset().toInt(), 0)
                                }
                                .anchoredDraggable(
                                    state = state,
                                    orientation = Orientation.Horizontal,
                                    flingBehavior = flingBehavior
                                )
                        ) {
                            ReviewItem(review, category, onClick = { onReviewClick(review) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review, category: Category?, onClick: () -> Unit) {
    val maroonColor = Color(0xFF703E4B)

    val allRatings = listOf(review.rating) + review.previousReviews.map { it.rating }
    val averageRating = allRatings.average()
    val formattedRating = (averageRating * 10).toInt() / 10.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (category != null) Color(category.color) else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (category != null) IconUtils.getIcon(category.iconName) else Icons.Default.Star,
                    contentDescription = null,
                    tint = maroonColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = category?.label ?: "Unknown",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Rating
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        color = maroonColor,
                        fontSize = 18.sp
                    )
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = maroonColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (review.previousReviews.isNotEmpty()) {
                    Text(
                        text = "Avg: $formattedRating",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
