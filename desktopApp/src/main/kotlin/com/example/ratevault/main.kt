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
            println("Desktop: Starting database export to $fileName")
            val dialog = FileDialog(null as Frame?, "Save Database", FileDialog.SAVE)
            dialog.file = fileName
            dialog.isVisible = true

            val directory = dialog.directory
            val file = dialog.file
            if (directory == null || file == null) {
                println("Desktop: Export cancelled by user")
                return false
            }
            println("Desktop: Exporting to $directory$file")

            return try {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "ratevault.db")
                dbFile.copyTo(File(directory, file), overwrite = true)
                println("Desktop: Database export successful")
                true
            } catch (e: Exception) {
                println("Desktop: Error exporting database: ${e.message}")
                false
            }
        }

        override suspend fun importDatabaseFile(): Boolean {
            println("Desktop: Starting database import")
            val dialog = FileDialog(null as Frame?, "Select Database", FileDialog.LOAD)
            dialog.isVisible = true

            val directory = dialog.directory
            val file = dialog.file
            if (directory == null || file == null) {
                println("Desktop: Import cancelled by user")
                return false
            }
            println("Desktop: Importing from $directory$file")

            return try {
                val dbFile = File(System.getProperty("java.io.tmpdir"), "ratevault.db")
                // Delete auxiliary files
                println("Desktop: Deleting auxiliary DB files...")
                File(System.getProperty("java.io.tmpdir"), "ratevault.db-wal").delete()
                File(System.getProperty("java.io.tmpdir"), "ratevault.db-shm").delete()
                
                File(directory, file).copyTo(dbFile, overwrite = true)
                println("Desktop: Database import successful")
                
                // On Desktop, we might need a way to restart or just notify the user.
                // For now, let's just return true and assume the app state is updated (if possible) 
                // or the user will restart.
                true
            } catch (e: Exception) {
                println("Desktop: Error importing database: ${e.message}")
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
