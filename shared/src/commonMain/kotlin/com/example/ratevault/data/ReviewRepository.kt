package com.example.ratevault.data

import com.example.ratevault.model.Category
import com.example.ratevault.model.Review
import com.example.ratevault.model.Tag
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val database: RateVaultDatabase) {
    private val reviewDao = database.reviewDao()
    private val categoryDao = database.categoryDao()
    private val tagDao = database.tagDao()

    fun getAllReviews(): Flow<List<Review>> = reviewDao.getAllReviews()
    
    suspend fun saveReview(review: Review) {
        reviewDao.insert(review)
        // Sync tags to the master Tag table
        review.tags.forEach { tagName ->
            if (tagDao.getTagByName(tagName) == null) {
                tagDao.insert(Tag(name = tagName))
            }
        }
    }

    suspend fun deleteReview(review: Review) {
        reviewDao.delete(review)
    }
    
    suspend fun getReviewByNameAndCategoryId(name: String, categoryId: Long): Review? {
        return reviewDao.getReviewByNameAndCategoryId(name, categoryId)
    }

    // Categories
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    suspend fun saveCategory(category: Category) = categoryDao.insert(category)
    
    suspend fun deleteCategory(category: Category): Boolean {
        val count = reviewDao.getReviewCountWithCategory(category.id)
        return if (count == 0) {
            categoryDao.delete(category)
            true
        } else {
            false
        }
    }

    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)

    // Tags
    fun getAllTags(): Flow<List<Tag>> = tagDao.getAllTags()
    suspend fun saveTag(tag: Tag) = tagDao.insert(tag)
    
    suspend fun deleteTag(tag: Tag): Boolean {
        val count = reviewDao.getReviewCountWithTag(tag.name)
        return if (count == 0) {
            tagDao.delete(tag)
            true
        } else {
            false
        }
    }

    suspend fun prepopulateIfNeeded() {
        val currentCategories = categoryDao.getAllCategoriesList()
        if (currentCategories.isEmpty()) {
            categoryDao.insert(Category(label = "Food", iconName = "Restaurant", color = 0xFFFCE4EC))
            categoryDao.insert(Category(label = "Place", iconName = "Place", color = 0xFFE8EAF6))
            categoryDao.insert(Category(label = "Moment", iconName = "Celebration", color = 0xFFE8F5E9))
        }
    }

    fun closeDatabase() {
        database.close()
    }
}
