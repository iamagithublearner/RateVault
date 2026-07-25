package com.example.ratevault

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratevault.data.ReviewRepository
import com.example.ratevault.ui.RateVaultScreen
import com.example.ratevault.ui.theme.RateVaultTheme

@Composable
fun App(repository: ReviewRepository) {
    RateVaultTheme {
        RateVaultScreen(repository)
    }
}
