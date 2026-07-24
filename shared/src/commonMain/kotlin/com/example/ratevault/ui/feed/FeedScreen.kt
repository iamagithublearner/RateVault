package com.example.ratevault.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun FeedScreen() {
    val materialColor = MaterialTheme.colorScheme
    val materialType = MaterialTheme.typography
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Recent Reviews" , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center , style = materialType.headlineLarge)
    }
}