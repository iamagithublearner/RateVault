package com.example.ratevault.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.ratevault.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category")
    suspend fun getAllCategoriesList(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?
}
