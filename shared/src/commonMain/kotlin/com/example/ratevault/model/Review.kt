package com.example.ratevault.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

enum class ReviewCategory(
    val label: String,
    val icon: ImageVector,
    val color: Long // Hex color for the icon background when not selected
) {
    Food("Food", Icons.Default.Restaurant, 0xFFFCE4EC),
    Place("Place", Icons.Default.Place, 0xFFE8EAF6),
    Moment("Moment", Icons.Default.Celebration, 0xFFE8F5E9)
}

@Entity
@Serializable
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val category: ReviewCategory = ReviewCategory.Place,
    val rating: Int = 0,
    val notes: String = "",
    val imagePath: String? = null,
    val location: String = "",
    val date: String = "",
    val tags: List<String> = emptyList(),
    val previousReviews: List<ReviewEntry> = emptyList()
)

@Serializable
data class ReviewEntry(
    val date: String,
    val rating: Int,
    val notes: String
)
