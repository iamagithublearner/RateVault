package com.example.ratevault.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL as libraryExecSQL
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import kotlinx.cinterop.ExperimentalForeignApi

fun getDatabaseBuilder(): RoomDatabase.Builder<RateVaultDatabase> {
    val dbFilePath = documentDirectory() + "/ratevault.db"
    return Room.databaseBuilder<RateVaultDatabase>(
        name = dbFilePath,
        factory = { RateVaultDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
}

fun provideReviewRepository(): ReviewRepository {
    return createReviewRepository(getDatabaseBuilder())
}

actual suspend fun SQLiteConnection.execSQL(sql: String) {
    this.libraryExecSQL(sql)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
