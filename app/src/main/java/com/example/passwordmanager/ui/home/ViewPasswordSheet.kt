package com.example.passwordmanager.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.passwordmanager.data.PasswordEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPasswordSheet(
    entity: PasswordEntity,
    decryptedPassword: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Account Details", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(20.dp))

            Text("Account Type", color = MaterialTheme.colorScheme.outline)
            Text(entity.accountType, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))

            Text("Username/ Email", color = MaterialTheme.colorScheme.outline)
            Text(entity.username, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))

            Text("Password", color = MaterialTheme.colorScheme.outline)
            // To exactly match screenshot: masked; to show real password, use decryptedPassword
            Text(decryptedPassword, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Edit")
                }

                Button(
                    onClick = onDelete,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Delete")
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
