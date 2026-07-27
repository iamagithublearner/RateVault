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
        override suspend fun exportDatabaseFile(fileName: String): Boolean {
            val dialog = FileDialog(null as Frame?, "Save Database", FileDialog.SAVE)
            dialog.file = fileName
            dialog.isVisible = true

            val directory = dialog.directory ?: return false
            val file = dialog.file ?: return false

            return try {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "ratevault.db")
                dbFile.copyTo(File(directory, file), overwrite = true)
                true
            } catch (e: Exception) {
                false
            }
        }

        override suspend fun importDatabaseFile(): Boolean {
            val dialog = FileDialog(null as Frame?, "Select Database", FileDialog.LOAD)
            dialog.isVisible = true

            val directory = dialog.directory ?: return false
            val file = dialog.file ?: return false

            return try {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "ratevault.db")
                // Delete auxiliary files
                File(System.getProperty("java.io.tmpdir"), "ratevault.db-wal").delete()
                File(System.getProperty("java.io.tmpdir"), "ratevault.db-shm").delete()
                
                File(directory, file).copyTo(dbFile, overwrite = true)
                
                // On Desktop, we might need a way to restart or just notify the user.
                // For now, let's just return true and assume the app state is updated (if possible) 
                // or the user will restart.
                true
            } catch (e: Exception) {
                false
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
