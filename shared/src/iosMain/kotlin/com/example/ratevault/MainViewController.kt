package com.example.ratevault

import androidx.compose.ui.window.ComposeUIViewController
import com.example.ratevault.data.provideReviewRepository

fun MainViewController() = ComposeUIViewController { 
    val repository = provideReviewRepository()
    App(repository) 
}
