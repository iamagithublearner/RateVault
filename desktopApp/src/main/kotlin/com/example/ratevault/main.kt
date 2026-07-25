package com.example.ratevault

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.ratevault.data.provideReviewRepository
import com.example.ratevault.data.PlatformBackupManager
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

fun main() = application {
    val repository = provideReviewRepository()
    
    val backupManager = object : PlatformBackupManager {
        override suspend fun exportData(json: String, fileName: String): Boolean {
            val dialog = FileDialog(null as Frame?, "Save Backup", FileDialog.SAVE)
            dialog.file = fileName
            dialog.isVisible = true
            
            val directory = dialog.directory ?: return false
            val file = dialog.file ?: return false
            
            return try {
                File(directory, file).writeText(json)
                true
            } catch (e: Exception) {
                false
            }
        }

        override suspend fun importData(): String? {
            val dialog = FileDialog(null as Frame?, "Select Backup", FileDialog.LOAD)
            dialog.isVisible = true
            
            val directory = dialog.directory ?: return null
            val file = dialog.file ?: return null
            
            return try {
                File(directory, file).readText()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "RateVault",
    ) {
        App(repository, backupManager)
    }
}
