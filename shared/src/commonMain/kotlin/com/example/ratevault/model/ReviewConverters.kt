package com.example.ratevault.model

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReviewConverters {
    @ColumnTypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @ColumnTypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @ColumnTypeConverter
    fun fromReviewEntryList(value: List<ReviewEntry>): String {
        return Json.encodeToString(value)
    }

    @ColumnTypeConverter
    fun toReviewEntryList(value: String): List<ReviewEntry> {
        return Json.decodeFromString(value)
    }
}
