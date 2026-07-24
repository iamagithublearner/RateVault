package com.example.ratevault.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    review: Review,
    onBack: () -> Unit,
    onReviewAgain: () -> Unit
) {
    val maroonColor = Color(0xFF703E4B)

    val allRatings = listOf(review.rating) + review.previousReviews.map { it.rating }
    val averageRating = allRatings.average()
    val totalReviews = allRatings.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Review Detail",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = maroonColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = maroonColor)
                    }
                },
                actions = {
                    Button(
                        onClick = onReviewAgain,
                        colors = ButtonDefaults.buttonColors(containerColor = maroonColor),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Review Again", fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            item {
                // Main Image & Category Tag
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.LightGray)
                ) {
                    // Placeholder for image
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp).align(Alignment.Center),
                        tint = Color.Gray
                    )

                    // Category Tag
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = review.category.icon,
                                contentDescription = null,
                                tint = maroonColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = review.category.label.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = maroonColor
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Date and Rating
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "${review.date.ifEmpty { "Recently" }} ${if (review.previousReviews.isNotEmpty()) "(Latest)" else ""}",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < averageRating.toInt()) maroonColor else Color.LightGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${(averageRating * 10).toInt() / 10.0} ($totalReviews)",
                                fontWeight = FontWeight.Bold,
                                color = maroonColor,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Title
                    Text(
                        text = review.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp,
                        color = Color.Black
                    )

                    // Location
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(review.location.ifEmpty { "Location not specified" }, color = Color.Gray, fontSize = 14.sp)
                    }

                    // Large Review Again Button
                    Button(
                        onClick = onReviewAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = maroonColor)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Review Again")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    // Flavor Profile / Tags
                    Text(
                        "TAGS / PROFILE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        val displayTags = if (review.tags.isEmpty()) listOf("Quality", "Service") else review.tags
                        displayTags.forEach { tag ->
                            TagItem(tag)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Notes Quote
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFDF2F4),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(maroonColor)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "\"${review.notes.ifEmpty { "No notes provided for this review." }}\"",
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic,
                                color = maroonColor,
                                lineHeight = 24.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Previous Reviews
                    Text(
                        "PREVIOUS REVIEWS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(16.dp))
                    
                    if (review.previousReviews.isEmpty()) {
                        Text("This is your first review of ${review.name}.", color = Color.Gray, fontSize = 14.sp)
                    } else {
                        review.previousReviews.forEach { entry ->
                            TimelineItem(entry, maroonColor)
                        }
                    }
                    
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    mainAxisSpacing: androidx.compose.ui.unit.Dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(mainAxisSpacing),
        verticalArrangement = Arrangement.spacedBy(crossAxisSpacing)
    ) {
        content()
    }
}

@Composable
fun TagItem(tag: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFCE4EC), // Light pink background
        modifier = Modifier.border(1.dp, Color(0xFFF19CAF).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Tag, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF703E4B))
            Spacer(Modifier.width(4.dp))
            Text(tag, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF703E4B))
        }
    }
}

@Composable
fun TimelineItem(entry: ReviewEntry, maroonColor: Color) {
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(maroonColor)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(Color.LightGray)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(entry.date, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < entry.rating) maroonColor else Color.LightGray,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(
                text = "\"${entry.notes}\"",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray
            )
        }
    }
}
