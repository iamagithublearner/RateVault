package com.example.ratevault.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.data.ReviewRepository
import com.example.ratevault.model.Category
import com.example.ratevault.ui.components.RateVaultTopAppBar
import com.example.ratevault.ui.utils.IconUtils
import kotlinx.coroutines.launch

@Composable
fun ManageCategoriesScreen(
    repository: ReviewRepository,
    onBack: () -> Unit
) {
    val categories by repository.getAllCategories().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var showAddDialog by remember { mutableStateOf(false) }
    val maroonColor = Color(0xFF703E4B)

    Scaffold(
        topBar = {
            RateVaultTopAppBar(
                title = "Manage Categories",
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = maroonColor)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = maroonColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryRow(
                    category = category,
                    onDelete = {
                        coroutineScope.launch {
                            repository.deleteCategory(category)
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AddCategoryDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { label, iconName, color ->
                coroutineScope.launch {
                    repository.saveCategory(Category(label = label, iconName = iconName, color = color))
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun CategoryRow(category: Category, onDelete: () -> Unit) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(category.color)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = IconUtils.getIcon(category.iconName),
                        contentDescription = null,
                        tint = Color(0xFF703E4B),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = category.label, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, onAdd: (String, String, Long) -> Unit) {
    var label by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("Restaurant") }
    var selectedColor by remember { mutableStateOf(0xFFFCE4EC) }

    val colors = listOf(0xFFFCE4EC, 0xFFE8EAF6, 0xFFE8F5E9, 0xFFFFF3E0, 0xFFF3E5F5, 0xFFE1F5FE)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Category Label") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Select Icon", fontWeight = FontWeight.Bold)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(48.dp),
                    modifier = Modifier.height(150.dp)
                ) {
                    items(IconUtils.categoryIcons.keys.toList()) { iconName ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(if (selectedIcon == iconName) Color.LightGray else Color.Transparent)
                                .clickable { selectedIcon = iconName },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(IconUtils.getIcon(iconName), contentDescription = iconName)
                        }
                    }
                }

                Text("Select Color", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(color))
                                .border(
                                    width = if (selectedColor == color) 2.dp else 0.dp,
                                    color = if (selectedColor == color) Color.Black else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(label, selectedIcon, selectedColor) },
                enabled = label.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
