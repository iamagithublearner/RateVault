package com.example.ratevault.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratevault.data.PlatformBackupManager
import com.example.ratevault.data.ReviewRepository
import com.example.ratevault.ui.components.RateVaultTopAppBar
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    repository: ReviewRepository, 
    backupManager: PlatformBackupManager,
    onManageCategories: () -> Unit,
    onManageTags: () -> Unit
) {
    val maroonColor = Color(0xFF703E4B)
    val bgColor = Color(0xFFFFF8F8)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = bgColor,
        topBar = { RateVaultTopAppBar(title = "Settings") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileCard()

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSection(title = "PREFERENCES") {
                var darkMode by remember { mutableStateOf(false) }
                SettingsToggleItem(
                    icon = Icons.Default.DarkMode,
                    label = "Dark Mode",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
                SettingsActionItem(
                    icon = Icons.Default.Palette,
                    label = "Change Theme",
                    trailing = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(maroonColor)
                        )
                    },
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "DATA MANAGEMENT") {
                SettingsActionItem(
                    icon = Icons.Default.FileUpload,
                    label = "Export Database File",
                    onClick = {
                        coroutineScope.launch {
                            val success = backupManager.exportDatabaseFile("ratevault.db")
                            snackbarHostState.showSnackbar(
                                if (success) "Database exported successfully" else "Failed to export database"
                            )
                        }
                    }
                )
                SettingsActionItem(
                    icon = Icons.Default.FileDownload,
                    label = "Import Database File",
                    onClick = {
                        coroutineScope.launch {
                            // We close the DB before importing to allow overwriting the file.
                            repository.closeDatabase()
                            val success = backupManager.importDatabaseFile()
                            if (success) {
                                snackbarHostState.showSnackbar("Database imported. Restarting...")
                            } else {
                                snackbarHostState.showSnackbar("Failed to import database")
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "CONTENT MANAGEMENT") {
                SettingsActionItem(
                    icon = Icons.Default.Category,
                    label = "Manage Categories",
                    onClick = onManageCategories
                )
                SettingsActionItem(
                    icon = Icons.AutoMirrored.Filled.Label,
                    label = "Manage Tags",
                    onClick = onManageTags
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "SUPPORT") {
                SettingsActionItem(
                    icon = Icons.Default.Info,
                    label = "About",
                    onClick = {
                        //TODO: display version information
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SignOutButton(maroonColor)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Version 2.4.0 (Hazakura Edition)",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun ProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5E6E8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person2,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF703E4B)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Username",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF8D5868),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF703E4B)
            )
        )
    }
}

@Composable
private fun SettingsActionItem(
    icon: ImageVector,
    label: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailing != null) {
                trailing()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
private fun SignOutButton(maroonColor: Color) {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, maroonColor, RoundedCornerShape(28.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = maroonColor
        ),
        shape = RoundedCornerShape(28.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign Out", fontWeight = FontWeight.Bold)
        }
    }
}
