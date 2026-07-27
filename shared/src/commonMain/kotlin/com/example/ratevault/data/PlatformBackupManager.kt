package com.example.ratevault.data

interface PlatformBackupManager {
    suspend fun exportDatabaseFile(fileName: String): Boolean
    suspend fun importDatabaseFile(): Boolean
}
