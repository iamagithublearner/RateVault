package com.example.ratevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratevault.data.provideReviewRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val repository = provideReviewRepository(this)

        setContent {
            App(repository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // App() // Needs mock repo for preview
}
