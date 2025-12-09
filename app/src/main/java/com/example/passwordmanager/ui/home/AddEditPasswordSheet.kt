package com.example.passwordmanager.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.data.PasswordEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordSheet(
    entity: PasswordEntity?,
    onSave: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var account by remember { mutableStateOf(entity?.accountType ?: "") }
    var username by remember { mutableStateOf(entity?.username ?: "") }
    var password by remember { mutableStateOf("") } // user sets / changes manually or via generator

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            OutlinedTextField(
                value = account,
                onValueChange = { account = it },
                label = { Text("Account Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username/ Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Password strength meter
            PasswordStrengthMeter(password = password)

            // Password generator button
            PasswordGeneratorSection { generated ->
                password = generated
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave(account, username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(if (entity == null) "Add New Account" else "Save Changes")
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
