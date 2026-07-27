package com.example.ratevault.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ratevault.data.ReviewRepository
import com.example.ratevault.model.Tag
import com.example.ratevault.ui.components.RateVaultTopAppBar
import kotlinx.coroutines.launch

@Composable
fun ManageTagsScreen(
    repository: ReviewRepository,
    onBack: () -> Unit
) {
    val tags by repository.getAllTags().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val maroonColor = Color(0xFF703E4B)

    Scaffold(
        topBar = {
            RateVaultTopAppBar(
                title = "Manage Tags",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = maroonColor)
                    }
                }
            )
        }
    ) { innerPadding ->
        if (tags.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No tags found. Tags are created when you add them to a review.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tags) { tag ->
                    TagRow(
                        tag = tag,
                        onDelete = {
                            coroutineScope.launch {
                                repository.deleteTag(tag)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TagRow(tag: Tag, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = tag.name, style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}
