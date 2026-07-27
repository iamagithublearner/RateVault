package com.example.ratevault.data

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL as libraryExecSQL

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<RateVaultDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("ratevault.db")
    return Room.databaseBuilder<RateVaultDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
        factory = { RateVaultDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
}

fun provideReviewRepository(context: Context): ReviewRepository {
    return createReviewRepository(getDatabaseBuilder(context))
}

actual suspend fun SQLiteConnection.execSQL(sql: String) {
    this.libraryExecSQL(sql)
}
