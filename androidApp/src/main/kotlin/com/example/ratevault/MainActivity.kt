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
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/x-sqlite3"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }

            exportDbDeferred = CompletableDeferred()
            createDbDocumentLauncher.launch(intent)

            val uri = exportDbDeferred?.await() ?: return false

            return try {
                val dbFile = getDatabasePath("ratevault.db")
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    dbFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }

        override suspend fun importDatabaseFile(): Boolean {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }

            importDbDeferred = CompletableDeferred()
            openDbDocumentLauncher.launch(intent)

            val uri = importDbDeferred?.await() ?: return false

            return try {
                val dbFile = getDatabasePath("ratevault.db")
                // Delete auxiliary files to avoid conflicts with the new database file
                getDatabasePath("ratevault.db-wal").delete()
                getDatabasePath("ratevault.db-shm").delete()

                contentResolver.openInputStream(uri)?.use { inputStream ->
                    dbFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                
                // Restart activity
                val restartIntent = packageManager.getLaunchIntentForPackage(packageName)
                restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
                startActivity(restartIntent)
                true
            } catch (e: Exception) {
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
