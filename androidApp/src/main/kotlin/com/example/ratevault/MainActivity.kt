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
    
    private var exportDeferred: CompletableDeferred<Uri?>? = null
    private var importDeferred: CompletableDeferred<Uri?>? = null

    private val createDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        exportDeferred?.complete(result.data?.data)
    }

    private val openDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        importDeferred?.complete(result.data?.data)
    }

    private val backupManager = object : PlatformBackupManager {
        override suspend fun exportData(json: String, fileName: String): Boolean {
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            
            exportDeferred = CompletableDeferred()
            createDocumentLauncher.launch(intent)
            
            val uri = exportDeferred?.await() ?: return false
            
            return try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(json.toByteArray())
                }
                true
            } catch (e: Exception) {
                false
            }
        }

        override suspend fun importData(): String? {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            }
            
            importDeferred = CompletableDeferred()
            openDocumentLauncher.launch(intent)
            
            val uri = importDeferred?.await() ?: return null
            
            return try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.readBytes().decodeToString()
                }
            } catch (e: Exception) {
                null
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
