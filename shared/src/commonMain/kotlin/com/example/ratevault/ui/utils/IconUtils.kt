package com.example.ratevault.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconUtils {
    val categoryIcons = mapOf(
        "Restaurant" to Icons.Default.Restaurant,
        "Place" to Icons.Default.Place,
        "Celebration" to Icons.Default.Celebration,
        "Movie" to Icons.Default.Movie,
        "Book" to Icons.Default.Book,
        "MusicNote" to Icons.Default.MusicNote,
        "Sports" to Icons.Default.Sports,
        "ShoppingBag" to Icons.Default.ShoppingBag,
        "Flight" to Icons.Default.Flight,
        "Hotel" to Icons.Default.Hotel,
        "Pets" to Icons.Default.Pets,
        "LocalCafe" to Icons.Default.LocalCafe
    )

    fun getIcon(name: String): ImageVector {
        return categoryIcons[name] ?: Icons.Default.Category
    }
}
