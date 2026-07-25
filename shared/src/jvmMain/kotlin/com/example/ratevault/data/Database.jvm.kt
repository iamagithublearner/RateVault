package com.example.ratevault.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<RateVaultDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "ratevault.db")
    return Room.databaseBuilder<RateVaultDatabase>(
        name = dbFile.absolutePath,
        factory = { RateVaultDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
}

fun provideReviewRepository(): ReviewRepository {
    return createReviewRepository(getDatabaseBuilder())
}
