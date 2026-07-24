package com.example.ratevault.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewCategory
import kotlin.collections.emptyList

fun createReview(index: Int) = Review(
    name = "Food $index",
    category = ReviewCategory.Food,
    rating = (index % 5) + 1,
    notes = "Notes for review $index",
    imagePath = null,
    location = "Location $index",
    date = "${index + 1} May 1969",
    tags = emptyList(),
    previousReviews = emptyList()
)

class ReviewPreviewProvider : PreviewParameterProvider<List<Review>> {
    override val values: Sequence<List<Review>> = sequenceOf(
        listOf(
            Review(
                name = "Place 1",
                category = ReviewCategory.Place,
                rating = 4,
                notes = "Notes",
                imagePath = null,
                location = "India",
                date = "25 May 1969",
                tags = emptyList(),
                previousReviews = emptyList()
            ),
            Review(
                name = "Food 2",
                category = ReviewCategory.Food,
                rating = 2,
                notes = "Notes",
                imagePath = null,
                location = "India",
                date = "25 May 1969",
                tags = emptyList(),
                previousReviews = emptyList()
            )
        ),
        emptyList(),
        (1..20).map(::createReview)


    )

}

@Preview
@Composable
fun ReviewsScreen(
    @PreviewParameter(ReviewPreviewProvider::class)
    reviews: List<Review>,
    onReviewClick: (Review) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Reviews",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF703E4B),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (reviews.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No reviews yet. Tap + to add one!", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(reviews) { review ->
                    ReviewItem(review, onClick = { onReviewClick(review) })
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review, onClick: () -> Unit) {
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
                    .background(Color(review.category.color)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = review.category.icon,
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
                    text = review.category.label,
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
