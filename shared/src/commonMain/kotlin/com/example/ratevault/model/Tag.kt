package com.example.ratevault.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)
