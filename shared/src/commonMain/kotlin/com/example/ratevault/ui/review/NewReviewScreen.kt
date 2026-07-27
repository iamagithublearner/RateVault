package com.example.ratevault.ui.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.model.Category
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratevault.ui.components.RateVaultTopAppBar
import com.example.ratevault.ui.utils.IconUtils

@Composable
fun NewReviewScreen(
    onDismiss: () -> Unit,
    onSave: (String, Category, Int, String, String, List<String>) -> Unit,
    categories: List<Category>,
    initialName: String = "",
    initialCategory: Category? = null,
    initialLocation: String = "",
    initialTags: List<String> = emptyList()
) {
    var reviewItemName by remember { mutableStateOf(initialName) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var rating by remember { mutableStateOf(0) }
    var location by remember { mutableStateOf(initialLocation) }
    var tagsInput by remember { mutableStateOf(initialTags.joinToString(", ")) }
    var notes by remember { mutableStateOf("") }

    val isSaveEnabled = reviewItemName.isNotBlank() && selectedCategory != null && rating > 0

    val maroonColor = Color(0xFF703E4B)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            RateVaultTopAppBar(
                title = "New Review",
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = maroonColor)
                    }
                },
                actions = null
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Name Input
            Text("What are you reviewing?", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = reviewItemName,
                onValueChange = { if (initialName.isBlank()) reviewItemName = it },
                readOnly = initialName.isNotBlank(),
                placeholder = { Text("e.g., The French Laundry, Inc", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (initialName.isNotBlank()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    unfocusedContainerColor = if (initialName.isNotBlank()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = if (initialName.isNotBlank()) Color.Gray else Color.Black,
                    unfocusedTextColor = if (initialName.isNotBlank()) Color.Gray else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selector
            Text("Category", color = Color.Gray, fontSize = 14.sp)
            if (categories.isEmpty()) {
                Text("No categories found. Please add them in Settings.", color = maroonColor, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categories.forEach { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory?.id == category.id,
                            enabled = initialCategory == null || initialCategory.id == category.id,
                            onClick = { 
                                if (initialCategory == null) {
                                    selectedCategory = category 
                                }
                            },
                            modifier = Modifier.weight(1f),
                            maroonColor = maroonColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rating Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("RATING", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            val isFilled = index < rating
                            Icon(
                                imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (isFilled) maroonColor else maroonColor.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { rating = index + 1 }
                            )
                        }
                    }
                    Text(
                        text = if (rating > 0) "$rating.0 - ${getRatingLabel(rating)}" else "Select Rating",
                        color = if (rating > 0) maroonColor else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Memory Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { /* Handle photo upload */ },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.Gray)
                    Text("Add a memory", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location Input
            Text("Location", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = location,
                onValueChange = { if (initialLocation.isBlank()) location = it },
                readOnly = initialLocation.isNotBlank(),
                placeholder = { Text("e.g., Austin, TX", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (initialLocation.isNotBlank()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    unfocusedContainerColor = if (initialLocation.isNotBlank()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = if (initialLocation.isNotBlank()) Color.Gray else Color.Black,
                    unfocusedTextColor = if (initialLocation.isNotBlank()) Color.Gray else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tags Input
            Text("Tags (comma separated)", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = tagsInput,
                onValueChange = { if (initialTags.isEmpty()) tagsInput = it },
                readOnly = initialTags.isNotEmpty(),
                placeholder = { Text("e.g., Savory, Spicy, Great View", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = if (initialTags.isNotEmpty()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    unfocusedContainerColor = if (initialTags.isNotEmpty()) Color(0xFFEEEEEE) else Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = if (initialTags.isNotEmpty()) Color.Gray else Color.Black,
                    unfocusedTextColor = if (initialTags.isNotEmpty()) Color.Gray else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notes Input
            Text("Notes & Thoughts", color = Color.Gray, fontSize = 14.sp)
            TextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("What stood out to you?...", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = { 
                    val tags = tagsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    onSave(reviewItemName, selectedCategory!!, rating, notes, location, tags) 
                },
                enabled = isSaveEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = maroonColor,
                    disabledContainerColor = maroonColor.copy(alpha = 0.5f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Entry", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    maroonColor: Color
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected && enabled -> maroonColor
                isSelected && !enabled -> maroonColor.copy(alpha = 0.5f)
                else -> Color.White
            }
        ),
        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) {
                            Color.White.copy(alpha = if (enabled) 0.2f else 0.1f)
                        } else {
                            Color(category.color).copy(alpha = if (enabled) 1.0f else 0.5f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = IconUtils.getIcon(category.iconName),
                    contentDescription = null,
                    tint = if (isSelected) Color.White else maroonColor.copy(alpha = if (enabled) 1.0f else 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.label,
                color = if (isSelected) {
                    Color.White.copy(alpha = if (enabled) 1.0f else 0.6f)
                } else {
                    Color.Black.copy(alpha = if (enabled) 1.0f else 0.4f)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun getRatingLabel(rating: Int): String = when (rating) {
    1 -> "Poor"
    2 -> "Fair"
    3 -> "Good"
    4 -> "Excellent"
    5 -> "Amazing"
    else -> ""
}

@Preview
@Composable
fun NewReviewScreenPreview() {
    NewReviewScreen(onDismiss = {}, onSave = { _, _, _, _, _, _ -> }, categories = emptyList())
}
