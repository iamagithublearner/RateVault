package com.example.ratevault.data

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import com.example.ratevault.model.Category
import com.example.ratevault.model.Review
import com.example.ratevault.model.ReviewConverters
import com.example.ratevault.model.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Review::class, Category::class, Tag::class], version = 2)
@ColumnTypeConverters(ReviewConverters::class)
@ConstructedBy(RateVaultDatabaseConstructor::class)
abstract class RateVaultDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        // 1. Create Category and Tag tables
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Category` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `label` TEXT NOT NULL, `iconName` TEXT NOT NULL, `color` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Tag` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")

        // 2. Insert default categories to map old reviews
        connection.execSQL("INSERT INTO Category (label, iconName, color) VALUES ('Food', 'Restaurant', 4294894828)")
        connection.execSQL("INSERT INTO Category (label, iconName, color) VALUES ('Place', 'Place', 4292865782)")
        connection.execSQL("INSERT INTO Category (label, iconName, color) VALUES ('Moment', 'Celebration', 4293457385)")

        // 3. Create new Review table
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Review_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `categoryId` INTEGER NOT NULL, `rating` INTEGER NOT NULL, `notes` TEXT NOT NULL, `imagePath` TEXT, `location` TEXT NOT NULL, `date` TEXT NOT NULL, `tags` TEXT NOT NULL, `previousReviews` TEXT NOT NULL)")

        // 4. Migrate data from old Review table to new Review table
        // We join with Category to get the new categoryId based on the old category string
        connection.execSQL("""
            INSERT INTO Review_new (id, name, categoryId, rating, notes, imagePath, location, date, tags, previousReviews)
            SELECT r.id, r.name, c.id, r.rating, r.notes, r.imagePath, r.location, r.date, r.tags, r.previousReviews
            FROM Review r JOIN Category c ON r.category = c.label
        """.trimIndent())

        // 5. Replace old table with new one
        connection.execSQL("DROP TABLE Review")
        connection.execSQL("ALTER TABLE Review_new RENAME TO Review")
    }
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<RateVaultDatabase>
): RateVaultDatabase {
    return builder
        .addMigrations(MIGRATION_1_2)
        .setQueryCoroutineContext(Dispatchers.Default)
        .addCallback(object : RoomDatabase.Callback() {
            override suspend fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                // We'll handle prepopulation via the repository on first access if needed
                // Or we can use raw SQL here if we know the schema
            }
        })
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

expect suspend fun SQLiteConnection.execSQL(sql: String)
