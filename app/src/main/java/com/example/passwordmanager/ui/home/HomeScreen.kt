package com.example.passwordmanager.ui.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.data.PasswordEntity
import com.example.passwordmanager.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PasswordViewModel) {

    val passwords by viewModel.passwords.collectAsState()
    val context = LocalContext.current

    var showAddEditSheet by remember { mutableStateOf(false) }
    var showDetailsSheet by remember { mutableStateOf(false) }
    var editingEntity by remember { mutableStateOf<PasswordEntity?>(null) }
    var selectedEntity by remember { mutableStateOf<PasswordEntity?>(null) }

    Scaffold(
        topBar = {
            // Figma-style title at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Password Manager",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        floatingActionButton = {
            // Blue rounded square "+" like Figma
            FloatingActionButton(
                onClick = {
                    editingEntity = null
                    showAddEditSheet = true
                },
                shape = MaterialTheme.shapes.large,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->

        if (passwords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No passwords yet. Tap + to add.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                items(passwords, key = { it.id }) { item ->
                    PasswordListCard(
                        entity = item,
                        onClick = {
                            selectedEntity = item
                            showDetailsSheet = true
                        }
                    )
                }
            }
        }

        // Bottom sheet: add / edit
        if (showAddEditSheet) {
            AddEditPasswordSheet(
                entity = editingEntity,
                onSave = { account, user, pass ->
                    if (account.isBlank() || user.isBlank() || pass.isBlank()) {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                    } else {
                        if (editingEntity == null) {
                            viewModel.addPassword(account, user, pass)
                        } else {
                            viewModel.updatePassword(editingEntity!!, account, user, pass)
                        }
                        showAddEditSheet = false
                    }
                },
                onDismiss = { showAddEditSheet = false }
            )
        }

        // Bottom sheet: details view (Account Details + Edit/Delete)
        if (showDetailsSheet && selectedEntity != null) {
            ViewPasswordSheet(
                entity = selectedEntity!!,
                decryptedPassword = viewModel.decryptedPassword(selectedEntity!!),
                onEdit = {
                    editingEntity = selectedEntity
                    showDetailsSheet = false
                    showAddEditSheet = true
                },
                onDelete = {
                    viewModel.deletePassword(selectedEntity!!)
                    showDetailsSheet = false
                },
                onDismiss = { showDetailsSheet = false }
            )
        }
    }
}

@Composable
private fun PasswordListCard(
    entity: PasswordEntity,
    onClick: () -> Unit
) {
    // Pill-shaped card just like Figma
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(entity.accountType, style = MaterialTheme.typography.titleMedium)
            Text("••••••••", color = MaterialTheme.colorScheme.outline)
        }
    }
}
