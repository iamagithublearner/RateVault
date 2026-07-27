package com.example.ratevault

import androidx.compose.ui.window.ComposeUIViewController
import com.example.ratevault.data.provideReviewRepository

import com.example.ratevault.data.PlatformBackupManager

fun MainViewController() = ComposeUIViewController { 
    val repository = provideReviewRepository()
    val backupManager = object : PlatformBackupManager {
        override suspend fun exportDatabaseFile(fileName: String): Boolean = false
        override suspend fun importDatabaseFile(): Boolean = false
    }
    App(repository, backupManager) 
}
