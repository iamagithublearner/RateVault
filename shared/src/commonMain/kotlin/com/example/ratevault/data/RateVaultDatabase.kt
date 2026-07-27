package com.example.ratevault.data

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewConverters
import kotlinx.coroutines.Dispatchers

@Database(entities = [Review::class], version = 1)
@ColumnTypeConverters(ReviewConverters::class)
@ConstructedBy(RateVaultDatabaseConstructor::class)
abstract class RateVaultDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<RateVaultDatabase>
): RateVaultDatabase {
    return builder
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}

fun createReviewRepository(builder: RoomDatabase.Builder<RateVaultDatabase>): ReviewRepository {
    val db = getRoomDatabase(builder)
    return ReviewRepository(db)
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object RateVaultDatabaseConstructor : RoomDatabaseConstructor<RateVaultDatabase> {
    override fun initialize(): RateVaultDatabase
}
