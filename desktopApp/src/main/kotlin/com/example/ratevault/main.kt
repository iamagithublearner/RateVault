package com.example.ratevault

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.ratevault.data.provideReviewRepository

fun main() = application {
    val repository = provideReviewRepository()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "RateVault",
    ) {
        App(repository)
    }
}
