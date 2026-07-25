package com.example.ratevault.data

import com.example.ratevault.model.Review
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val reviewDao: ReviewDao) {
    fun getAllReviews(): Flow<List<Review>> = reviewDao.getAllReviews()

    suspend fun saveReview(review: Review) {
        reviewDao.insert(review)
    }

    suspend fun deleteReview(review: Review) {
        reviewDao.delete(review)
    }
    
    suspend fun getReviewByNameAndCategory(name: String, category: String): Review? {
        return reviewDao.getReviewByNameAndCategory(name, category)
    }
}
