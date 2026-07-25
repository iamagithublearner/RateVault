package com.example.ratevault.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewCategory
import com.example.ratevault.ui.components.RateVaultTopAppBar
import com.example.ratevault.ui.review.ReviewPreviewProvider

@Preview
@Composable
fun FeedScreen(
    @PreviewParameter(ReviewPreviewProvider::class)
    reviews: List<Review>,
    onReviewClick: (Review) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<ReviewCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F8))
    ) {
        RateVaultTopAppBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                CategoriesSection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = if (selectedCategory == it) null else it
                    },
                    onShuffleClick = {
                        val filtered = if (selectedCategory != null) {
                            reviews.filter { it.category == selectedCategory }
                        } else {
                            reviews
                        }
                        if (filtered.isNotEmpty()) {
                            onReviewClick(filtered.random())
                        }
                    }
                )
            }

            item {
                Text(
                    text = if (selectedCategory != null) "Reviews for ${selectedCategory?.label}" else "Recent Reviews",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            val filteredReviews = if (selectedCategory != null) {
                reviews.filter { it.category == selectedCategory }
            } else {
                reviews
            }

            items(filteredReviews) { review ->
                ReviewCard(
                    review = review,
                    onClick = { onReviewClick(review) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}


@Composable
private fun CategoriesSection(
    selectedCategory: ReviewCategory?,
    onCategorySelected: (ReviewCategory) -> Unit,
    onShuffleClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onShuffleClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Random Review",
                        tint = Color(0xFF703E4B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = "Manage",
                    color = Color(0xFF8D5868),
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF8D5868)
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ReviewCategory.entries) { category ->
                CategoryChip(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: ReviewCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val maroonColor = Color(0xFF703E4B)
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) maroonColor else Color(category.color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isSelected) Color.White else maroonColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

@Composable
private fun ReviewCard(
    review: Review,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
       StandardReviewCard(review, onClick, modifier)
}

@Composable
private fun StandardReviewCard(
    review: Review,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maroonColor = Color(0xFF703E4B)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(review.category.color)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = review.category.icon,
                        contentDescription = null,
                        tint = maroonColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Rating in the top right
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        color = maroonColor,
                        fontSize = 16.sp
                    )
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = maroonColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = review.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            if (review.notes.isNotEmpty()) {
                Text(
                    text = review.notes,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.date,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
