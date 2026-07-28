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

    @Query("SELECT * FROM Review WHERE name = :name AND categoryId = :categoryId")
    suspend fun getReviewByNameAndCategoryId(name: String, categoryId: Long): Review?

    @Query("SELECT COUNT(*) FROM Review WHERE (json_valid(tags) AND EXISTS (SELECT 1 FROM json_each(tags) WHERE LOWER(value) = LOWER(:tagName))) OR (NOT json_valid(tags) AND (tags = :tagName OR tags LIKE :tagName || ',%' OR tags LIKE '%,' || :tagName || ',%' OR tags LIKE '%,' || :tagName))")
    suspend fun getReviewCountWithTag(tagName: String): Int

    @Query("SELECT COUNT(*) FROM Review WHERE categoryId = :categoryId")
    suspend fun getReviewCountWithCategory(categoryId: Long): Int
}
