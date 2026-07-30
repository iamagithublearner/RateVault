package com.example.ratevault

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratevault.data.provideReviewRepository
import com.example.ratevault.data.PlatformBackupManager
import kotlinx.coroutines.CompletableDeferred
import java.io.OutputStream
import java.io.InputStream

class MainActivity : ComponentActivity() {
    
    private var exportDbDeferred: CompletableDeferred<Uri?>? = null
    private var importDbDeferred: CompletableDeferred<Uri?>? = null

    private val createDbDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        exportDbDeferred?.complete(result.data?.data)
    }

    private val openDbDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        importDbDeferred?.complete(result.data?.data)
    }

    private val backupManager = object : PlatformBackupManager {
        override suspend fun exportDatabaseFile(fileName: String): Boolean {
            println("MainActivity: exportDatabaseFile called for $fileName")
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/x-sqlite3"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }

            exportDbDeferred = CompletableDeferred()
            createDbDocumentLauncher.launch(intent)

            val uri = exportDbDeferred?.await()
            if (uri == null) {
                println("MainActivity: Export cancelled by user")
                return false
            }
            println("MainActivity: Export destination URI: $uri")

            return try {
                val dbFile = getDatabasePath("ratevault.db")
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    dbFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                println("MainActivity: Database export successful")
                true
            } catch (e: Exception) {
                println("MainActivity: Error exporting database: ${e.message}")
                false
            }
        }

        override suspend fun importDatabaseFile(): Boolean {
            println("MainActivity: importDatabaseFile called")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }

            importDbDeferred = CompletableDeferred()
            openDbDocumentLauncher.launch(intent)

            val uri = importDbDeferred?.await()
            if (uri == null) {
                println("MainActivity: Import cancelled by user")
                return false
            }
            println("MainActivity: Import source URI: $uri")

            return try {
                val dbFile = getDatabasePath("ratevault.db")
                // Delete auxiliary files to avoid conflicts with the new database file
                println("MainActivity: Deleting auxiliary DB files...")
                getDatabasePath("ratevault.db-wal").delete()
                getDatabasePath("ratevault.db-shm").delete()

                contentResolver.openInputStream(uri)?.use { inputStream ->
                    dbFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                
                println("MainActivity: Database import successful, restarting activity...")
                // Restart activity
                val restartIntent = packageManager.getLaunchIntentForPackage(packageName)
                restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(restartIntent)
                true
            } catch (e: Exception) {
                println("MainActivity: Error importing database: ${e.message}")
                false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val repository = provideReviewRepository(this)

        setContent {
            App(repository, backupManager)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // App() // Needs mock repo for preview
}
