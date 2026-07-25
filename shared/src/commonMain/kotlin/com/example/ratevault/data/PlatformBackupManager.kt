package com.example.ratevault.data

interface PlatformBackupManager {
    suspend fun exportData(json: String, fileName: String): Boolean
    suspend fun importData(): String?
}
