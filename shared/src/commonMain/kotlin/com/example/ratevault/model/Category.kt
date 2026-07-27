package com.example.ratevault.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val iconName: String,
    val color: Long
)
