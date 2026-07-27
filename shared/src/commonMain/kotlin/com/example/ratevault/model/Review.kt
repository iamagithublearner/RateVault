package com.example.ratevault.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val categoryId: Long = 0,
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
