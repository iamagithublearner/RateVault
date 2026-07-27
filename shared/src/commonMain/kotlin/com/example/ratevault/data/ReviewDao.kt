package com.example.ratevault.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.ratevault.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: Review)

    @Delete
    suspend fun delete(review: Review)

    @Query("SELECT * FROM Review ORDER BY date DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM Review WHERE name = :name AND category = :category")
    suspend fun getReviewByNameAndCategory(name: String, category: String): Review?
}
